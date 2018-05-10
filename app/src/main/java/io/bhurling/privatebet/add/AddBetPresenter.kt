package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.common.Optional
import io.bhurling.privatebet.common.get
import io.bhurling.privatebet.common.isSome
import io.bhurling.privatebet.common.none
import io.bhurling.privatebet.friends.InvitationsInteractor
import io.bhurling.privatebet.model.pojo.Person
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject

class AddBetPresenter constructor(
        private val interactor: InvitationsInteractor // TODO should not reference this one
) : Presenter<AddBetPresenter.View>() {

    private val viewStateSubject = PublishSubject.create<ViewState>()

    override fun attachView(view: View) {
        super.attachView(view)

        disposables += Observables
                .combineLatest(
                        interactor.confirmed(),
                        viewStateSubject
                )
                .take(1) // TODO Allow updates to confirmed friends while on screen
                .map { (opponentIds, state) ->
                    state.copy(opponentIds = opponentIds)
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.backClicks().map { state }
                }
                .flatMap { state ->
                    when (state.step) {
                        ViewStateStep.STATEMENT -> {
                            Observable.empty<ViewState>().doOnSubscribe { view.finish() }
                        }
                        ViewStateStep.STAKE -> {
                            Observable.just(state.copy(step = ViewStateStep.STATEMENT))
                        }
                        ViewStateStep.OPPONENT -> {
                            if (state.opponent != null) {
                                Observable.just(state.copy(opponent = null))
                            } else {
                                Observable.just(state.copy(step = ViewStateStep.STAKE))
                            }
                        }
                    }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.deadlineChanges()
                            .map { state.copy(deadline = it) }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.clearDeadlineClicks()
                            .map { state.copy(deadline = none()) }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.nextClicks().map {
                        when (state.step) {
                            ViewStateStep.STATEMENT -> {
                                state.copy(
                                        statement = view.getStatement().trim(),
                                        step = ViewStateStep.STAKE
                                )
                            }
                            ViewStateStep.STAKE -> {
                                state.copy(
                                        stake = view.getStake().trim(),
                                        step = ViewStateStep.OPPONENT
                                )
                            }
                            else -> state
                        }
                    }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .switchMap { state ->
                    view.opponentSelected()
                            .map { state.copy(opponent = it) }
                }
                .subscribe { viewStateSubject.onNext(it) }

        disposables += viewStateSubject
                .map { it.deadline }
                .distinctUntilChanged()
                .switchMap { deadline -> view.deadlineClicks().map { deadline } }
                .subscribe { view.showDeadlinePicker(it) }

        disposables += viewStateSubject
                .map { it.deadline }
                .distinctUntilChanged()
                .subscribe {
                    if (it.isSome) {
                        view.setDeadline(it.get())
                    } else {
                        view.removeDeadline()
                    }
                }

        disposables += viewStateSubject
                .map { it.step }
                .distinctUntilChanged()
                .subscribe {
                    view.showStep(it)
                }

        disposables += viewStateSubject
                .map { it.step }
                .distinctUntilChanged()
                .subscribe {
                    updateButtonVisibility(it)
                }

        disposables += viewStateSubject
                .map { it.statement to it.opponent }
                .distinctUntilChanged()
                .subscribe { (statement, opponent) ->
                    updateSummary(statement, opponent)
                }

        disposables += viewStateSubject
                .map { it.step to it.opponent }
                .distinctUntilChanged()
                .subscribe { (step, opponent) ->
                    updateSummaryVisibility(step, opponent)
                }

        disposables += viewStateSubject
                .map { it.opponentIds }
                .distinctUntilChanged()
                .map { it.map { OpponentsAdapterItem(it) } }
                .subscribe {
                    view.updateOpponents(it)
                }

        viewStateSubject.onNext(ViewState())
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
