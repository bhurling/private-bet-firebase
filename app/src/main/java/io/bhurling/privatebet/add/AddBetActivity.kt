package io.bhurling.privatebet.add

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding2.view.clicks
import io.bhurling.privatebet.R
import io.bhurling.privatebet.arch.*
import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.common.ui.datePickerDialog
import io.bhurling.privatebet.common.ui.doOnNextLayoutOrImmediate
import io.bhurling.privatebet.model.pojo.Person
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView
import org.koin.inject
import java.util.*
import java.util.concurrent.TimeUnit

class AddBetActivity : AppCompatActivity(), AddBetPresenter.View {
    private val presenter: AddBetPresenter by inject()
    private val adapter: OpponentsAdapter by inject()

    private val toolbar: Toolbar by bindView(R.id.bets_add_toolbar)
    private val pager: ViewPager by bindView(R.id.bets_add_pager)
    private val statement: EditText by bindView(R.id.bets_add_statement)
    private val deadline: TextView by bindView(R.id.bets_add_deadline)
    private val clearDeadline: View by bindView(R.id.bets_add_deadline_remove)
    private val stake: EditText by bindView(R.id.bets_add_stake)
    private val opponents: RecyclerView by bindView(R.id.bets_add_opponent_list)
    private val next: View by bindView(R.id.bets_add_next)
    private val summary by lazy {
        SummaryViewHolder(findViewById(R.id.bets_add_summary_root))
    }

    private val actions = PublishSubject.create<AddAction>()
    private val disposables = CompositeDisposable()

    private val inputManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        pager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any) = view == `object`

            override fun getCount() = 3

            override fun instantiateItem(container: ViewGroup, position: Int) = container.getChildAt(position)
        }

        pager.offscreenPageLimit = 2

        // TODO can we move this out?
        statement.setHorizontallyScrolling(false)
        statement.maxLines = 100
        stake.setHorizontallyScrolling(false)
        stake.maxLines = 100

        opponents.layoutManager = LinearLayoutManager(this)
        opponents.adapter = adapter

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
        presenter.detachView()

        disposables.dispose()

        super.onDestroy()
    }

    override fun actions(): Observable<AddAction> = actions

    override fun getStatement() = statement.text.toString()

    override fun getStake() = stake.text.toString()

    private fun attach() {
        presenter.attachView(this)

        disposables += deadline.clicks()
            .subscribe { actions.onNext(AddAction.DeadlineClicked) }

        disposables += clearDeadline.clicks()
            .delay(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                actions.onNext(AddAction.DeadlineCleared)
            }

        disposables += next.clicks()
            .subscribe {
                actions.onNext(AddAction.NextClicked)
            }

        disposables += adapter.actions()
            .ofType<OpponentsAction.Selected>()
            .subscribe {
                actions.onNext(AddAction.OpponentSelected(it.person))
            }

        disposables += presenter.states
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { render(it) }

        disposables += presenter.effects
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handle(it) }
    }

    private fun render(state: AddViewState) {
        updateStep(state.step)
        updateDeadline(state.deadline)
        updateOpponents(state.opponentIds)
        updateSummary(state.step, state.statement, state.opponent)
        updateButton(state.shouldShowNextButton)
    }

    private fun updateStep(step: AddViewState.Step) {
        when (step) {
            AddViewState.Step.STATEMENT -> pager.setCurrentItem(0, true)
            AddViewState.Step.STAKE -> pager.setCurrentItem(1, true)
            AddViewState.Step.OPPONENT -> {
                pager.setCurrentItem(2, true)

                inputManager.hideSoftInputFromWindow(opponents.windowToken, 0)
            }
        }
    }

    private fun updateDeadline(value: Optional<Long>) {
        deadline.text = value.getOrNull()?.let { deadline ->
            DateFormat.getMediumDateFormat(this).format(deadline)
        } ?: getString(R.string.no_deadline)

        clearDeadline.isVisible = value.isSome
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
        if (summary.root.isVisible) return

        summary.root.visibility = View.VISIBLE

        adapter.items.indexOfFirst { it.id == opponentId }.takeUnless { it == -1 }?.let { index ->
            summary.opponent.doOnNextLayoutOrImmediate { opponent ->
                val topBefore = IntArray(2).apply {
                    opponents.layoutManager?.findViewByPosition(index)
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
        if (!summary.root.isVisible) return

        summary.root.isVisible = false
    }

    private fun updateButton(shouldShowNextButton: Boolean) {
        next.isVisible = shouldShowNextButton
    }

    private fun handle(effect: AddEffect) {
        when (effect) {
            is AddEffect.ShowDeadlinePicker -> showDeadlinePicker(effect.initialValue)
            is AddEffect.Finish -> finish()
        }
    }

    private fun showDeadlinePicker(initialValue: Calendar) {
        datePickerDialog(this, initialValue) { selected ->
            actions.onNext(AddAction.DeadlineChanged(selected.timeInMillis.toOptional()))
        }.apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }
}
