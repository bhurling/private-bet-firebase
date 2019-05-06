package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.Optional

sealed class AddEffect {
    data class ShowDeadlinePicker(val deadline: Optional<Long>) : AddEffect()
    object Finish : AddEffect()
}