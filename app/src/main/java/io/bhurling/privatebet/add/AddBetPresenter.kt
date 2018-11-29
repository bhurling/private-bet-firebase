package io.bhurling.privatebet.add

import com.airbnb.mvrx.RealMvRxStateStore
import io.bhurling.privatebet.arch.*
import io.bhurling.privatebet.friends.InvitationsInteractor
import io.bhurling.privatebet.model.pojo.Person
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign

class AddBetPresenter constructor(
        private val interactor: InvitationsInteractor // TODO should not reference this one
) : Presenter<AddBetPresenter.View>() {

    private val store = RealMvRxStateStore(ViewState())
    private val states = store.observable.observeOn(AndroidSchedulers.mainThread())

    override fun attachView(view: View) {
        super.attachView(view)

        disposables += interactor.confirmed()
                .take(1) // TODO Allow updates to confirmed friends while on screen
                .subscribe { opponentIds ->
                    store.set { copy(opponentIds = opponentIds) }
                }

        disposables += states
                .switchMap { state ->
                    view.backClicks().map { state }
                }
                .subscribe { state ->
                    when (state.step) {
                        ViewStateStep.STATEMENT -> {
                            view.finish()
                        }
                        ViewStateStep.STAKE -> {
                            store.set { copy(step = ViewStateStep.STATEMENT) }
                        }
                        ViewStateStep.OPPONENT -> {
                            if (state.opponent != null) {
                                store.set { copy(opponent = null) }
                            } else {
                                store.set { copy(step = ViewStateStep.STAKE) }
                            }
                        }
                    }
                }

        disposables += view.deadlineChanges()
                .subscribe { store.set { copy(deadline = it) } }

        disposables += view.clearDeadlineClicks()
                .subscribe { store.set { copy(deadline = none()) } }

        disposables += states
                .switchMap { state ->
                    view.nextClicks().map { state }
                }
                .subscribe { state ->
                    when (state.step) {
                        ViewStateStep.STATEMENT -> {
                            store.set {
                                copy(
                                        statement = view.getStatement().trim(),
                                        step = ViewStateStep.STAKE
                                )
                            }
                        }
                        ViewStateStep.STAKE -> {
                            store.set {
                                copy(
                                        stake = view.getStake().trim(),
                                        step = ViewStateStep.OPPONENT
                                )
                            }
                        }
                        else -> {
                            // ignore
                        }
                    }
                }

        disposables += view.opponentSelected()
                .subscribe {
                    store.set { copy(opponent = it) }
                }

        disposables += states
                .map { it.deadline }
                .distinctUntilChanged()
                .switchMap { deadline -> view.deadlineClicks().map { deadline } }
                .subscribe { view.showDeadlinePicker(it) }

        disposables += states
                .map { it.deadline }
                .distinctUntilChanged()
                .subscribe {
                    if (it.isSome) {
                        view.setDeadline(it.get())
                    } else {
                        view.removeDeadline()
                    }
                }

        disposables += states
                .map { it.step }
                .distinctUntilChanged()
                .subscribe {
                    view.showStep(it)
                }

        disposables += states
                .map { it.step }
                .distinctUntilChanged()
                .subscribe {
                    updateButtonVisibility(it)
                }

        disposables += states
                .map { it.statement to it.opponent }
                .distinctUntilChanged()
                .subscribe { (statement, opponent) ->
                    updateSummary(statement, opponent)
                }

        disposables += states
                .map { it.step to it.opponent }
                .distinctUntilChanged()
                .subscribe { (step, opponent) ->
                    updateSummaryVisibility(step, opponent)
                }

        disposables += states
                .map { it.opponentIds }
                .distinctUntilChanged()
                .map { it.map { OpponentsAdapterItem(it) } }
                .subscribe {
                    view.updateOpponents(it)
                }
    }

    private fun updateSummary(statement: String, opponent: Person?) {
        opponent?.let {
            view.setSummary(statement, it)
        }
    }

    private fun updateSummaryVisibility(step: ViewStateStep, opponent: Person?) {
        if (step == ViewStateStep.OPPONENT && opponent != null) {
            view.showSummary(opponent.id)
        } else {
            view.hideSummary()
        }
    }

    private fun updateButtonVisibility(step: ViewStateStep) {
        when (step) {
            ViewStateStep.OPPONENT -> view.hideNextButton()
            else -> view.showNextButton()
        }
    }

    data class ViewState(
            val step: ViewStateStep = ViewStateStep.STATEMENT,
            val statement: String = "",
            val deadline: Optional<Long> = none(),
            val stake: String = "",
            val opponentIds: List<String> = listOf(),
            val opponent: Person? = null
    )

    enum class ViewStateStep {
        STATEMENT,
        STAKE,
        OPPONENT
    }

    interface View : Presenter.View {
        fun backClicks(): Observable<Unit>
        fun finish()
        fun showStep(step: ViewStateStep)
        fun getStatement(): String
        fun deadlineClicks(): Observable<Unit>
        fun deadlineChanges(): Observable<Optional<Long>>
        fun showDeadlinePicker(currentDeadline: Optional<Long>)
        fun setDeadline(deadline: Long)
        fun removeDeadline()
        fun clearDeadlineClicks(): Observable<Unit>
        fun getStake(): String
        fun updateOpponents(opponents: List<OpponentsAdapterItem>)
        fun opponentSelected(): Observable<Person>
        fun setSummary(statement: String, opponent: Person)
        fun showSummary(opponentId: String)
        fun hideSummary()
        fun showNextButton()
        fun hideNextButton()
        fun nextClicks(): Observable<Unit>
    }
}
