package io.hurling.privatebet.feature.create

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    initialSelectedDate: LocalDate?,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDate?.toEpochMillis(),
        selectableDates = AfterToday
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it.toLocalDate())
                    }
                }
            ) {
                Text(text = stringResource(R.string.create_bet_deadline_dialog_confirm))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

private fun LocalDate.toEpochMillis(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

@OptIn(ExperimentalMaterial3Api::class)
private val AfterToday = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis > System.currentTimeMillis()
}