package io.bhurling.privatebet.add

import java.util.Calendar

sealed class AddEffect {
    data class ShowDeadlinePicker(val initialValue: Calendar) : AddEffect()
    data object Finish : AddEffect()
}
