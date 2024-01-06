package io.bhurling.privatebet.add

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.databinding.ActivityAddBinding
import io.bhurling.privatebet.model.pojo.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AddBetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private val viewModel: AddBetViewModel by viewModels()

    @Inject
    lateinit var adapter: OpponentsAdapter

    private val summary by lazy {
        SummaryViewHolder(binding.betsAddSummaryInclude.root)
    }

    private val summaryAnimator by lazy {
        SummaryAnimator(
            list = binding.betsAddOpponentInclude.betsAddOpponentList,
            summary = summary
        )
    }

    private val actions = MutableSharedFlow<AddAction>()
    private val disposables = CompositeDisposable()
    private val effectHandler = AddBetEffectHandler(this, actions)

    private val inputManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.betsAddToolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        binding.betsAddPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any) = view == `object`

            override fun getCount() = 3

            override fun instantiateItem(container: ViewGroup, position: Int) =
                container.getChildAt(position)
        }

        binding.betsAddPager.offscreenPageLimit = 2

        // Setting this programmatically to force "done" action for multiline text
        // https://stackoverflow.com/questions/36338563
        binding.betsAddStatementInclude.betsAddStatement.setHorizontallyScrolling(false)
        binding.betsAddStatementInclude.betsAddStatement.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.betsAddStatementInclude.betsAddStatement.maxLines = 10

        binding.betsAddStakeInclude.betsAddStake.setHorizontallyScrolling(false)
        binding.betsAddStakeInclude.betsAddStake.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.betsAddStakeInclude.betsAddStake.maxLines = 10

        binding.betsAddOpponentInclude.betsAddOpponentList.layoutManager = LinearLayoutManager(this)
        binding.betsAddOpponentInclude.betsAddOpponentList.adapter = adapter

        attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        viewModel.offer(AddAction.BackClicked)
    }

    override fun onDestroy() {
        disposables.dispose()

        super.onDestroy()
    }

    private fun attach() {
        disposables += binding.betsAddStatementInclude.betsAddStatement.textChanges()
            .map { it.toString() }
            .subscribe { viewModel.offer(AddAction.StatementChanged(it)) }

        disposables += binding.betsAddStakeInclude.betsAddStake.textChanges().map { it.toString() }
            .subscribe { viewModel.offer(AddAction.StakeChanged(it)) }

        disposables += binding.betsAddStatementInclude.betsAddDeadlineBg.clicks()
            .subscribe { viewModel.offer(AddAction.DeadlineClicked) }

        disposables += binding.betsAddStatementInclude.betsAddDeadlineRemove.clicks()
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.offer(AddAction.DeadlineCleared)
            }

        disposables += binding.betsAddNext.clicks()
            .subscribe {
                viewModel.offer(AddAction.NextClicked)
            }

        lifecycleScope.launch {
            adapter.actions()
                .filterIsInstance<OpponentsAction.Selected>()
                .collect {
                    viewModel.offer(AddAction.OpponentSelected(it.profile))
                }
        }

        lifecycleScope.launch {
            actions.collect(viewModel::offer)
        }

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .map { it.step }
                .distinctUntilChanged()
                .collect(::updateStep)
        }

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .map { it.deadline }
                .distinctUntilChanged()
                .collect(::updateDeadline)
        }

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .map { it.contacts }
                .distinctUntilChanged()
                .collect(::updateContacts)
        }

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .map { it.shouldShowNextButton }
                .distinctUntilChanged()
                .collect(::updateButton)
        }

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .collect { state ->
                    updateSummary(state.step, state.statement, state.opponent)
                }
        }

        lifecycleScope.launch {
            viewModel.effects.flowWithLifecycle(lifecycle)
                .collect(effectHandler::accept)
        }
    }

    private fun updateStep(step: AddViewState.Step) {
        when (step) {
            AddViewState.Step.STATEMENT -> {
                binding.betsAddPager.setCurrentItem(0, true)
                binding.betsAddStatementInclude.betsAddStatement.requestFocus()
            }

            AddViewState.Step.STAKE -> {
                binding.betsAddPager.setCurrentItem(1, true)
            }

            AddViewState.Step.OPPONENT -> {
                binding.betsAddPager.setCurrentItem(2, true)

                inputManager.hideSoftInputFromWindow(
                    binding.betsAddOpponentInclude.betsAddOpponentList.windowToken,
                    0
                )
            }
        }
    }

    private fun updateDeadline(value: Long?) {
        binding.betsAddStatementInclude.betsAddDeadline.text = value?.let { deadline ->
            DateFormat.getMediumDateFormat(this).format(deadline)
        } ?: getString(R.string.no_deadline)

        binding.betsAddStatementInclude.betsAddDeadlineRemove.isVisible = value != null
    }

    private fun updateContacts(contacts: List<Profile>) {
        adapter.items = contacts.map {
            OpponentsAdapterItem(
                profile = it
            )
        }
    }

    private fun updateSummary(step: AddViewState.Step, statement: String, opponent: Profile?) {
        opponent?.let { summary.bind(statement, it) }

        if (step == AddViewState.Step.OPPONENT && opponent != null) {
            summaryAnimator.show(opponent.id)
        } else {
            summaryAnimator.hide()
        }
    }

    private fun updateButton(shouldShowNextButton: Boolean) {
        binding.betsAddNext.isVisible = shouldShowNextButton
    }
}
