package io.bhurling.privatebet.add

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.Optional
import io.bhurling.privatebet.common.get
import io.bhurling.privatebet.common.isSome
import io.bhurling.privatebet.common.toOptional
import io.bhurling.privatebet.common.ui.datePickerDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotterknife.bindView
import org.koin.inject
import java.util.*
import java.util.concurrent.TimeUnit

class AddBetActivity : AppCompatActivity(), AddBetPresenter.View {
    private val presenter: AddBetPresenter by inject()

    private val toolbar: Toolbar by bindView(R.id.bets_add_toolbar)
    private val pager: ViewPager by bindView(R.id.bets_add_pager)
    private val statement: EditText by bindView(R.id.bets_add_statement)
    private val deadline: TextView by bindView(R.id.bets_add_deadline)
    private val clearDeadline: View by bindView(R.id.bets_add_deadline_remove)
    private val stake: EditText by bindView(R.id.bets_add_stake)
    private val next: View by bindView(R.id.bets_add_next)

    private val backClicks = PublishSubject.create<Unit>()
    private val deadlineChanges = PublishSubject.create<Optional<Long>>()

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

            override fun getCount() = 2

            override fun instantiateItem(container: ViewGroup, position: Int) = container.getChildAt(position)
        }

        statement.setHorizontallyScrolling(false)
        statement.maxLines = 100
        stake.setHorizontallyScrolling(false)
        stake.maxLines = 100

        presenter.attachView(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        backClicks.onNext(Unit)
    }

    override fun onDestroy() {
        presenter.detachView()

        super.onDestroy()
    }

    override fun backClicks(): Observable<Unit> = backClicks

    override fun showStep(step: AddBetPresenter.ViewStateStep) {
        when (step) {
            AddBetPresenter.ViewStateStep.STATEMENT -> pager.setCurrentItem(0, true)
            AddBetPresenter.ViewStateStep.STAKE -> pager.setCurrentItem(1, true)
        }
    }

    override fun getStatement() = statement.text.toString()

    override fun deadlineClicks() = deadline.clicks()

    override fun deadlineChanges(): Observable<Optional<Long>> = deadlineChanges

    override fun showDeadlinePicker(currentDeadline: Optional<Long>) {
        val currentCalendar = Calendar.getInstance().apply {
            if (currentDeadline.isSome) {
                timeInMillis = currentDeadline.get()
            }
        }

        datePickerDialog(this, currentCalendar, { selected ->
            deadlineChanges.onNext(selected.timeInMillis.toOptional())
        }).apply {
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

    override fun clearDeadlineClicks(): Observable<Unit> = clearDeadline.clicks()
            .delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

    override fun getStake() = stake.text.toString()

    override fun nextClicks() = next.clicks()
}
