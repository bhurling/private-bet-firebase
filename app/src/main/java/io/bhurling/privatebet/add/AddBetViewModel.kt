package io.bhurling.privatebet.add

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.common.arch.BaseViewModel
import io.bhurling.privatebet.friends.InvitationsRepository
import io.bhurling.privatebet.model.pojo.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddBetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: InvitationsRepository
) : BaseViewModel<AddViewState, AddViewState.StateUpdate, AddEffect, AddAction>(savedStateHandle) {

    init {
        offer(AddAction.Init)
    }
    override fun initialState() = AddViewState()

    override fun execute(action: AddAction): Flow<AddViewState.StateUpdate> {
        return when (action) {
            is AddAction.Init -> onInit()
            is AddAction.BackClicked -> onBackClicked()
            is AddAction.DeadlineChanged -> onDeadlineChanged(action.deadline)
            is AddAction.DeadlineCleared -> onDeadlinedCleared()
            is AddAction.DeadlineClicked -> onDeadlineClicked()
            is AddAction.NextClicked -> onNextClicked()
            is AddAction.OpponentSelected -> onOpponentSelected(action.opponent)
            is AddAction.StakeChanged -> onStakeChanged(action.stake)
            is AddAction.StatementChanged -> onStatementChanged(action.statement)
        }
    }

    private fun onInit(): Flow<AddViewState.StateUpdate> {
        return repository.confirmed()
            .map { confirmed -> confirmed.map(Profile::id) }
            .map(AddViewState.StateUpdate::OpponentIds)
    }

    private fun onDeadlineClicked(): Flow<AddViewState.StateUpdate> {
        return flow {
            withState { state ->
                val calendar = Calendar.getInstance().apply {
                    state.deadline?.let { deadline ->
                        timeInMillis = deadline
                    }
                }

                sendEffect(AddEffect.ShowDeadlinePicker(calendar))
            }
        }
    }

    private fun onDeadlinedCleared(): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.Deadline(deadline = null))
    }

    private fun onBackClicked(): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.MoveBack)
    }

    private fun onNextClicked(): Flow<AddViewState.StateUpdate> {
        return flow {
            withState { state ->
                emit(AddViewState.StateUpdate.Statement(state.statement.trim()))
                emit(AddViewState.StateUpdate.Stake(state.stake.trim()))
            }

            emit(AddViewState.StateUpdate.MoveForward)
        }
    }

    private fun onDeadlineChanged(deadline: Long): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.Deadline(deadline))
    }

    private fun onOpponentSelected(opponent: Profile): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.Opponent(opponent))
    }

    private fun onStakeChanged(stake: String): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.Stake(stake))
    }

    private fun onStatementChanged(statement: String): Flow<AddViewState.StateUpdate> {
        return flowOf(AddViewState.StateUpdate.Statement(statement))
    }

    override fun reduce(state: AddViewState, update: AddViewState.StateUpdate): AddViewState {
        return when (update) {
            is AddViewState.StateUpdate.Deadline -> state.copy(
                deadline = update.deadline
            )

            is AddViewState.StateUpdate.MoveBack -> when (state.step) {
                AddViewState.Step.STATEMENT -> state.also {
                    sendEffect(AddEffect.Finish)
                }

                AddViewState.Step.STAKE -> state.copy(
                    step = AddViewState.Step.STATEMENT
                )

                AddViewState.Step.OPPONENT -> state.copy(
                    step = AddViewState.Step.STAKE,
                    opponent = null
                )
            }

            is AddViewState.StateUpdate.MoveForward -> when (state.step) {
                AddViewState.Step.STATEMENT -> state.copy(
                    step = AddViewState.Step.STAKE,
                )

                AddViewState.Step.STAKE -> state.copy(
                    step = AddViewState.Step.OPPONENT,
                )

                AddViewState.Step.OPPONENT -> state
            }

            is AddViewState.StateUpdate.Opponent -> state.copy(
                opponent = update.opponent
            )

            is AddViewState.StateUpdate.Stake -> state.copy(
                stake = update.stake
            )

            is AddViewState.StateUpdate.Statement -> state.copy(
                statement = update.statement
            )

            is AddViewState.StateUpdate.OpponentIds -> state.copy(
                opponentIds = update.ids
            )
        }
    }
}
