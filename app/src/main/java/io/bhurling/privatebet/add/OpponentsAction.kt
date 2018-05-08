package io.bhurling.privatebet.add

import io.bhurling.privatebet.model.pojo.Person

sealed class OpponentsAction {
    data class Selected(val person: Person) : OpponentsAction()
}