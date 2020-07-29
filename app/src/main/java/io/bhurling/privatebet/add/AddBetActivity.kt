package io.bhurling.privatebet.add

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.bhurling.privatebet.R
import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.arch.getOrNull
import io.bhurling.privatebet.arch.isSome
import io.bhurling.privatebet.arch.toOptional
import io.bhurling.privatebet.model.pojo.Person
import io.bhurling.privatebet.ui.doOnNextLayoutOrImmediate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.partial_add_opponent.*
import kotlinx.android.synthetic.main.partial_add_stake.*
import kotlinx.android.synthetic.main.partial_add_statement.*
import kotlinx.android.synthetic.main.partial_add_summary.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class AddBetActivity : AppCompatActivity(R.layout.activity_add) {
    private val viewModel: AddBetViewModel by viewModel()
    private val adapter: OpponentsAdapter by inject()

    private val summary by lazy {
        SummaryViewHolder(bets_add_summary_root)
    }

    private val actions = PublishSubject.create<AddAction>()
    private val disposables = CompositeDisposable()

    private val inputManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(bets_add_toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        bets_add_pager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any) = view == `object`

            override fun getCount() = 3

            override fun instantiateItem(container: ViewGroup, position: Int) = container.getChildAt(position)
        }

        bets_add_pager.offscreenPageLimit = 2

        // Setting this programmatically to force "done" action for multiline text
        // https://stackoverflow.com/questions/36338563
        bets_add_statement.setHorizontallyScrolling(false)
        bets_add_statement.imeOptions = EditorInfo.IME_ACTION_DONE
        bets_add_statement.maxLines = 10

        bets_add_stake.setHorizontallyScrolling(false)
        bets_add_stake.imeOptions = EditorInfo.IME_ACTION_DONE
        bets_add_statement.maxLines = 10

        bets_add_opponent_list.layoutManager = LinearLayoutManager(this)
        bets_add_opponent_list.adapter = adapter

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
        actions.onNext(AddAction.BackClicked)
    }

    override fun onDestroy() {
        viewModel.detach()

        disposables.dispose()

        super.onDestroy()
    }

    private fun attach() {
        viewModel.attach(actions)

        disposables += bets_add_statement.textChanges().map { it.toString() }
            .subscribe { actions.onNext(AddAction.StatementChanged(it)) }

        disposables += bets_add_stake.textChanges().map { it.toString() }
            .subscribe { actions.onNext(AddAction.StakeChanged(it)) }

        disposables += bets_add_deadline_bg.clicks()
            .subscribe { actions.onNext(AddAction.DeadlineClicked) }

        disposables += bets_add_deadline_remove.clicks()
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                actions.onNext(AddAction.DeadlineCleared)
            }

        disposables += bets_add_next.clicks()
            .subscribe {
                actions.onNext(AddAction.NextClicked)
            }

        disposables += adapter.actions()
            .ofType<OpponentsAction.Selected>()
            .subscribe {
                actions.onNext(AddAction.OpponentSelected(it.person))
            }

        disposables += viewModel.stateOf { step }
            .subscribe(this::updateStep)

        disposables += viewModel.stateOf { deadline }
            .subscribe(this::updateDeadline)

        disposables += viewModel.stateOf { opponentIds }
            .subscribe(this::updateOpponents)

        disposables += viewModel.stateOf { shouldShowNextButton }
            .subscribe(this::updateButton)

        disposables += viewModel.states()
            .subscribe {
                updateSummary(it.step, it.statement, it.opponent)
            }

        disposables += viewModel.effects()
            .ofType<AddEffect.ShowDeadlinePicker>()
            .map { it.initialValue }
            .subscribe(this::showDeadlinePicker)

        disposables += viewModel.effects()
            .ofType<AddEffect.Finish>()
            .subscribe { finish() }
    }

    private fun updateStep(step: AddViewState.Step) {
        when (step) {
            AddViewState.Step.STATEMENT -> bets_add_pager.setCurrentItem(0, true)
            AddViewState.Step.STAKE -> bets_add_pager.setCurrentItem(1, true)
            AddViewState.Step.OPPONENT -> {
                bets_add_pager.setCurrentItem(2, true)

                inputManager.hideSoftInputFromWindow(bets_add_opponent_list.windowToken, 0)
            }
        }
    }

    private fun updateDeadline(value: Optional<Long>) {
        bets_add_deadline.text = value.getOrNull()?.let { deadline ->
            DateFormat.getMediumDateFormat(this).format(deadline)
        } ?: getString(R.string.no_deadline)

        bets_add_deadline_remove.isVisible = value.isSome
    }

    private fun updateOpponents(opponentIds: List<String>) {
        adapter.items = opponentIds.map { OpponentsAdapterItem(it) }
    }

    private fun updateSummary(step: AddViewState.Step, statement: String, opponent: Person?) {
        opponent?.let { summary.bind(statement, it) }

        if (step == AddViewState.Step.OPPONENT && opponent != null) {
            showSummary(opponent.id)
        } else {
            hideSummary()
        }
    }

    private fun showSummary(opponentId: String) {
        if (summary.containerView.isVisible) return

        summary.containerView.visibility = View.VISIBLE

        adapter.items.indexOfFirst { it.id == opponentId }.takeUnless { it == -1 }?.let { index ->
            summary.opponent.doOnNextLayoutOrImmediate { opponent ->
                val topBefore = IntArray(2).apply {
                    bets_add_opponent_list.layoutManager?.findViewByPosition(index)
                        ?.getLocationOnScreen(this)
                }[1]

                val topAfter = IntArray(2).apply {
                    opponent.getLocationOnScreen(this)
                }[1]

                opponent.translationY = (topBefore - topAfter).toFloat()
                opponent.animate().translationY(0F).start()
            }

            summary.fadeIn()
        }
    }

    private fun hideSummary() {
        if (!summary.containerView.isVisible) return

        summary.containerView.isVisible = false
    }

    private fun updateButton(shouldShowNextButton: Boolean) {
        bets_add_next.isVisible = shouldShowNextButton
    }

    private fun showDeadlinePicker(initialValue: Calendar) {
        datePickerDialog(this, initialValue) { selected ->
            actions.onNext(AddAction.DeadlineChanged(selected.timeInMillis.toOptional()))
        }.apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }
}
