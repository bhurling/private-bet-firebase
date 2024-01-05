package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.arch.ViewModelAction
import io.bhurling.privatebet.model.pojo.Profile

sealed class AddAction : ViewModelAction {
    object BackClicked : AddAction()
    data class StatementChanged(val statement: String) : AddAction()
    data class StakeChanged(val stake: String) : AddAction()
    object DeadlineClicked : AddAction()
    data class DeadlineChanged(val deadline: Optional<Long>) : AddAction()
    object DeadlineCleared : AddAction()
    data class OpponentSelected(val opponent: Profile) : AddAction()
    object NextClicked : AddAction()
}