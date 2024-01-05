package io.bhurling.privatebet.add

import io.bhurling.privatebet.model.pojo.Profile

sealed class OpponentsAction {
    data class Selected(val profile: Profile) : OpponentsAction()
}