package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.model.pojo.Person

sealed class AddAction {
    object BackClicked : AddAction()
    object DeadlineClicked : AddAction()
    data class DeadlineChanged(val deadline: Optional<Long>) : AddAction()
    object DeadlineCleared : AddAction()
    data class OpponentSelected(val opponent: Person) : AddAction()
    object NextClicked : AddAction()
}