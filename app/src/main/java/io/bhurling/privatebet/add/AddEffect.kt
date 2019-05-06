package io.bhurling.privatebet.add

import java.util.*

sealed class AddEffect {
    data class ShowDeadlinePicker(val initialValue: Calendar) : AddEffect()
    object Finish : AddEffect()
}