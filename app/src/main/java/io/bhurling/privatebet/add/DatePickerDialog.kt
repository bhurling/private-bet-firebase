package io.bhurling.privatebet.add

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import java.util.*

fun datePickerDialog(context: Context, current: Calendar, listener: (Calendar) -> Unit): DatePickerDialog {
    val realListener: (View, Int, Int, Int) -> Unit = { _, year, month, dayOfMonth ->
        val selectedDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        listener.invoke(selectedDate)
    }

    return DatePickerDialog(
            context,
            realListener,
            current.get(Calendar.YEAR),
            current.get(Calendar.MONTH),
            current.get(Calendar.DAY_OF_MONTH)
    )
}
