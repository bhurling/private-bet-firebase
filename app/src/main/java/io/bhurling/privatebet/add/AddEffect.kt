package io.bhurling.privatebet.add

import io.bhurling.privatebet.arch.ViewModelEffect
import java.util.*

sealed class AddEffect : ViewModelEffect {
    data class ShowDeadlinePicker(val initialValue: Calendar) : AddEffect()
    object Finish : AddEffect()
}