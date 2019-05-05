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
import androidx.core.view.doOnNextLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jakewharton.rxbinding2.view.clicks
import io.bhurling.privatebet.R
import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.arch.get
import io.bhurling.privatebet.arch.isSome
import io.bhurling.privatebet.arch.toOptional
import io.bhurling.privatebet.common.ui.datePickerDialog
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

    private val actions = PublishSubject.create<Action>()
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

        statement.setHorizontallyScrolling(false)
        statement.maxLines = 100
        stake.setHorizontallyScrolling(false)
        stake.maxLines = 100

        opponents.layoutManager = LinearLayoutManager(this)
        opponents.adapter = adapter

        presenter.attachView(this)

        disposables += deadline.clicks()
            .subscribe { actions.onNext(Action.DeadlineClicked) }

        disposables += clearDeadline.clicks()
            .delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                actions.onNext(Action.DeadlineCleared)
            }

        disposables += next.clicks()
            .subscribe {
                actions.onNext(Action.NextClicked)
            }

        disposables += adapter.actions()
            .ofType<OpponentsAction.Selected>()
            .subscribe {
                actions.onNext(Action.OpponentSelected(it.person))
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        actions.onNext(Action.BackClicked)
    }

    override fun onDestroy() {
        presenter.detachView()

        disposables.dispose()

        super.onDestroy()
    }

    override fun actions(): Observable<Action> = actions

    override fun showStep(step: AddBetPresenter.ViewStateStep) {
        when (step) {
            AddBetPresenter.ViewStateStep.STATEMENT -> pager.setCurrentItem(0, true)
            AddBetPresenter.ViewStateStep.STAKE -> pager.setCurrentItem(1, true)
            AddBetPresenter.ViewStateStep.OPPONENT -> {
                pager.setCurrentItem(2, true)
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        inputManager.hideSoftInputFromWindow(opponents.windowToken, 0)
    }

    override fun getStatement() = statement.text.toString()

    override fun showDeadlinePicker(currentDeadline: Optional<Long>) {
        val currentCalendar = Calendar.getInstance().apply {
            if (currentDeadline.isSome) {
                timeInMillis = currentDeadline.get()
            }
        }

        datePickerDialog(this, currentCalendar) { selected ->
            actions.onNext(Action.DeadlineChanged(selected.timeInMillis.toOptional()))
        }.apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }

    override fun setDeadline(deadline: Long) {
        this.deadline.text = DateFormat.getMediumDateFormat(this).format(deadline)
        this.clearDeadline.visibility = View.VISIBLE
    }

    override fun removeDeadline() {
        this.deadline.text = getString(R.string.no_deadline)
        this.clearDeadline.visibility = View.GONE
    }

    override fun getStake() = stake.text.toString()

    override fun updateOpponents(opponents: List<OpponentsAdapterItem>) {
        adapter.items = opponents
    }

    override fun setSummary(statement: String, opponent: Person) {
        summary.bind(statement, opponent)
    }

    override fun showSummary(opponentId: String) {
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

            arrayOf(summary.statement, summary.vs).forEach {
                it.alpha = 0F
                it.animate()
                        .alpha(1F)
                        .setStartDelay(200)
                        .withEndAction { it.alpha = 1F }
                        .start()
            }

            arrayOf(summary.button).forEach {
                it.alpha = 0F
                it.animate()
                        .alpha(1F)
                        .setStartDelay(300)
                        .withEndAction { it.alpha = 1F }
                        .start()
            }

        }
    }

    override fun hideSummary() {
        summary.root.visibility = View.GONE
    }

    override fun showNextButton() {
        TransitionManager.beginDelayedTransition(next.parent as ViewGroup)

        next.visibility = View.VISIBLE
    }

    override fun hideNextButton() {
        TransitionManager.beginDelayedTransition(next.parent as ViewGroup)

        next.visibility = View.INVISIBLE
    }
}

inline fun View.doOnNextLayoutOrImmediate(crossinline action: (view: View) -> Unit) {
    if (isLaidOut) {
        action(this)
    } else {
        doOnNextLayout(action)
    }
}
