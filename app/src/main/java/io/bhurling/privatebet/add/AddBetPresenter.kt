package io.bhurling.privatebet.add

import com.airbnb.mvrx.RealMvRxStateStore
import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.arch.none
import io.bhurling.privatebet.friends.InvitationsInteractor
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class AddBetPresenter constructor(
        private val interactor: InvitationsInteractor // TODO should not reference this one
) : Presenter<AddBetPresenter.View>() {

    private val store = RealMvRxStateStore(AddViewState())

    val states: Observable<AddViewState> = store.observable
    val effects: Subject<AddEffect> = PublishSubject.create<AddEffect>()

    override fun attachView(view: View) {
        super.attachView(view)

        disposables += interactor.confirmed()
                .take(1) // TODO Allow updates to confirmed friends while on screen
                .subscribe { opponentIds ->
                    store.set { copy(opponentIds = opponentIds) }
                }

        disposables += states
                .switchMap { state ->
                    view.actions()
                        .ofType<AddAction.BackClicked>()
                        .map { state }
                }
                .subscribe { state ->
                    when (state.step) {
                        AddViewState.Step.STATEMENT -> {
                            effects.onNext(AddEffect.Finish)
                        }
                        AddViewState.Step.STAKE -> {
                            store.set { copy(step = AddViewState.Step.STATEMENT) }
                        }
                        AddViewState.Step.OPPONENT -> {
                            if (state.opponent != null) {
                                store.set { copy(opponent = null) }
                            } else {
                                store.set { copy(step = AddViewState.Step.STAKE) }
                            }
                        }
                    }
                }

        disposables += view.actions()
                .ofType<AddAction.DeadlineChanged>()
                .subscribe { store.set { copy(deadline = it.deadline) } }

        disposables += view.actions()
                .ofType<AddAction.DeadlineCleared>()
                .subscribe { store.set { copy(deadline = none()) } }

        disposables += states
                .switchMap { state ->
                    view.actions()
                        .ofType<AddAction.NextClicked>()
                        .map { state }
                }
                .subscribe { state ->
                    when (state.step) {
                        AddViewState.Step.STATEMENT -> {
                            store.set {
                                copy(
                                        statement = view.getStatement().trim(),
                                        step = AddViewState.Step.STAKE
                                )
                            }
                        }
                        AddViewState.Step.STAKE -> {
                            store.set {
                                copy(
                                        stake = view.getStake().trim(),
                                        step = AddViewState.Step.OPPONENT
                                )
                            }
                        }
                        else -> {
                            // ignore
                        }
                    }
                }

        disposables += view.actions()
                .ofType<AddAction.OpponentSelected>()
                .subscribe {
                    store.set { copy(opponent = it.opponent) }
                }

        disposables += states
                .map { it.deadline }
                .distinctUntilChanged()
                .switchMap { deadline ->
                    view.actions()
                        .ofType<AddAction.DeadlineClicked>()
                        .map { deadline }
                }
                .subscribe {
                    effects.onNext(AddEffect.ShowDeadlinePicker(it))
                }
    }

    interface View : Presenter.View {
        // actions
        fun actions(): Observable<AddAction>
        fun getStake(): String
        fun getStatement(): String
    }
}
