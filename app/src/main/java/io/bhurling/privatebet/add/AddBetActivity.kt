package io.bhurling.privatebet.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
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
    private val statement: EditText by bindView(R.id.bets_add_statement)
    private val deadline: TextView by bindView(R.id.bets_add_deadline)
    private val clearDeadline: View by bindView(R.id.bets_add_deadline_remove)

    private val deadlineChanges = PublishSubject.create<Optional<Long>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        statement.setHorizontallyScrolling(false)
        statement.maxLines = 100

        presenter.attachView(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.detachView()

        super.onDestroy()
    }

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
}
