package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.model.pojo.Profile

sealed class AddAction {
    data object Init : AddAction()
    data object BackClicked : AddAction()
    data class StatementChanged(val statement: String) : AddAction()
    data class StakeChanged(val stake: String) : AddAction()
    data object DeadlineClicked : AddAction()
    data class DeadlineChanged(val deadline: Optional<Long>) : AddAction()
    data object DeadlineCleared : AddAction()
    data class OpponentSelected(val opponent: Profile) : AddAction()
    data object NextClicked : AddAction()
}