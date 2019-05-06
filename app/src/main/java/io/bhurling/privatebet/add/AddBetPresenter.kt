package io.bhurling.privatebet.add

import com.airbnb.mvrx.RealMvRxStateStore
import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.arch.getOrNull
import io.bhurling.privatebet.arch.none
import io.bhurling.privatebet.friends.InvitationsInteractor
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import java.util.*

class AddBetPresenter constructor(
    private val interactor: InvitationsInteractor // TODO should not reference this one
) : Presenter<AddBetPresenter.View>() {

    private val store = RealMvRxStateStore(AddViewState())
    private val effectsInternal = PublishSubject.create<AddEffect>()

    val states: Observable<AddViewState>
        get() = store.observable

    val effects: Observable<AddEffect>
        get() = effectsInternal.hide()

    override fun attachView(view: View) {
        super.attachView(view)

        attach(view.actions())
    }

    private fun attach(actions: Observable<AddAction>) {
        disposables += interactor.confirmed()
            .take(1) // TODO Allow updates to confirmed friends while on screen
            .subscribe { opponentIds ->
                store.set { copy(opponentIds = opponentIds) }
            }

        disposables += actions
            .ofType<AddAction.BackClicked>()
            .mapToLatestFrom(states)
            .subscribe { state ->
                when (state.step) {
                    AddViewState.Step.STATEMENT -> {
                        effectsInternal.onNext(AddEffect.Finish)
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

        disposables += actions
            .ofType<AddAction.DeadlineClicked>()
            .mapToLatestFrom(states)
            .map { state ->
                Calendar.getInstance().apply {
                    state.deadline.getOrNull()?.let { deadline ->
                        timeInMillis = deadline
                    }
                }
            }
            .subscribe {
                effectsInternal.onNext(AddEffect.ShowDeadlinePicker(it))
            }

        disposables += actions
            .ofType<AddAction.DeadlineChanged>()
            .subscribe { store.set { copy(deadline = it.deadline) } }

        disposables += actions
            .ofType<AddAction.DeadlineCleared>()
            .subscribe { store.set { copy(deadline = none()) } }

        disposables += actions
            .ofType<AddAction.NextClicked>()
            .mapToLatestFrom(states)
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

        disposables += actions
            .ofType<AddAction.OpponentSelected>()
            .subscribe {
                store.set { copy(opponent = it.opponent) }
            }
    }

    interface View : Presenter.View {
        // actions
        fun actions(): Observable<AddAction>
        fun getStake(): String
        fun getStatement(): String
    }
}

private fun <T, R> Observable<T>.mapToLatestFrom(other: Observable<R>): Observable<R> {
    return this.withLatestFrom(other).map { (_, other) -> other }
}
