package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional
import io.bhurling.privatebet.model.pojo.Person

sealed class Action {
    object BackClicked : Action()
    object DeadlineClicked : Action()
    data class DeadlineChanged(val deadline: Optional<Long>) : Action()
    object DeadlineCleared : Action()
    data class OpponentSelected(val opponent: Person) : Action()
    object NextClicked : Action()
}