package io.bhurling.privatebet.add

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import io.reactivex.functions.Consumer
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import java.util.Calendar

class AddBetEffectHandler(
    private val activity: ComponentActivity,
    private val collector: FlowCollector<AddAction>
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
            activity.lifecycleScope.launch {
                collector.emit(AddAction.DeadlineChanged(selected.timeInMillis))
            }
        }.apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }
}
