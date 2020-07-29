package io.bhurling.privatebet.add

import android.app.Activity
import io.bhurling.privatebet.arch.toOptional
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import java.util.*

class AddBetEffectHandler(
    private val activity: Activity,
    private val observer: Observer<AddAction>
) : Consumer<AddEffect> {
    override fun accept(effect: AddEffect) {
        when (effect) {
            is AddEffect.Finish -> onFinish()
            is AddEffect.ShowDeadlinePicker -> onShowDeadlinePicker(effect.initialValue)
        }
    }

    private fun onFinish() {
        activity.finish()
    }

    private fun onShowDeadlinePicker(initialValue: Calendar) {
        datePickerDialog(activity, initialValue) { selected ->
            observer.onNext(AddAction.DeadlineChanged(selected.timeInMillis.toOptional()))
        }.apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }
}
