package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.BaseViewModel
import io.bhurling.privatebet.arch.getOrNull
import io.bhurling.privatebet.arch.none
import io.bhurling.privatebet.friends.InvitationsInteractor
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.withLatestFrom
import java.util.*

class AddBetViewModel constructor(
    private val interactor: InvitationsInteractor // TODO should not reference this one
) : BaseViewModel<AddAction, AddViewState, AddEffect>(AddViewState()) {

    override fun handleActions(actions: Observable<AddAction>) {
        disposables += interactor.confirmed()
            .take(1) // TODO Allow updates to confirmed friends while on screen
            .subscribe { opponentIds ->
                updateState { copy(opponentIds = opponentIds) }
            }

        disposables += actions
            .ofType<AddAction.BackClicked>()
            .mapToLatestFrom(states())
            .subscribe { state ->
                when (state.step) {
                    AddViewState.Step.STATEMENT -> {
                        sendEffect(AddEffect.Finish)
                    }
                    AddViewState.Step.STAKE -> {
                        updateState { copy(step = AddViewState.Step.STATEMENT) }
                    }
                    AddViewState.Step.OPPONENT -> {
                        if (state.opponent != null) {
                            updateState { copy(opponent = null) }
                        } else {
                            updateState { copy(step = AddViewState.Step.STAKE) }
                        }
                    }
                }
            }

        disposables += actions
            .ofType<AddAction.DeadlineClicked>()
            .mapToLatestFrom(states())
            .map { state ->
                Calendar.getInstance().apply {
                    state.deadline.getOrNull()?.let { deadline ->
                        timeInMillis = deadline
                    }
                }
            }
            .subscribe {
                sendEffect(AddEffect.ShowDeadlinePicker(it))
            }

        disposables += actions
            .ofType<AddAction.DeadlineChanged>()
            .subscribe { updateState { copy(deadline = it.deadline) } }

        disposables += actions
            .ofType<AddAction.DeadlineCleared>()
            .subscribe { updateState { copy(deadline = none()) } }

        disposables += actions
            .ofType<AddAction.NextClicked>()
            .mapToLatestFrom(states())
            .filter { state -> state.step == AddViewState.Step.STATEMENT }
            .mapToLatestFrom(
                actions.ofType<AddAction.StatementChanged>()
                    .map { it.statement }
            )
            .subscribe { statement ->
                updateState {
                    copy(
                        statement = statement.trim(),
                        step = AddViewState.Step.STAKE
                    )
                }
            }

        disposables += actions
            .ofType<AddAction.NextClicked>()
            .mapToLatestFrom(states())
            .filter { state -> state.step == AddViewState.Step.STAKE }
            .mapToLatestFrom(
                actions.ofType<AddAction.StakeChanged>()
                    .map { it.stake }
            )
            .subscribe { stake ->
                updateState {
                    copy(
                        stake = stake.trim(),
                        step = AddViewState.Step.OPPONENT
                    )
                }
            }

        disposables += actions
            .ofType<AddAction.OpponentSelected>()
            .subscribe {
                updateState { copy(opponent = it.opponent) }
            }
    }
}

private fun <T : Any, R : Any> Observable<T>.mapToLatestFrom(other: Observable<R>): Observable<R> {
    return this.withLatestFrom(other).map { (_, other) -> other }
}
