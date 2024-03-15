package io.hurling.privatebet.feature.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.hurling.privatebet.core.design.PreviewScaffold
import io.hurling.privatebet.core.design.autoFocus
import java.time.LocalDate

@Composable
internal fun CreateBetScreen() {
    val viewModel: CreateBetViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateBet(
        state = state,
        onStatementChanged = viewModel::onStatementChanged,
        onDeadlineChanged = viewModel::onDeadlineChanged
    )
}

@Composable
private fun CreateBet(
    state: CreateBetScreenState,
    onStatementChanged: (String) -> Unit = {},
    onDeadlineChanged: (LocalDate) -> Unit = {}
) {
    var shouldShowDatePicker by remember {
        mutableStateOf(false)
    }

    // TODO human readable date
    val deadlineString = state.deadline?.toString()
        ?: stringResource(id = R.string.create_bet_no_deadline)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .autoFocus(),
                value = state.statement,
                onValueChange = onStatementChanged,
                label = {
                    Text(text = stringResource(R.string.create_bet_statement_label))
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            // TODO loose focus when dialog is closed
                            if (it.isFocused) shouldShowDatePicker = true
                        },
                    readOnly = true,
                    value = deadlineString,
                    onValueChange = {},
                    label = {
                        Text(text = stringResource(R.string.create_bet_deadline_label))
                    }
                )
            }

            if (shouldShowDatePicker) {
                DeadlinePicker(
                    initialSelectedDate = state.deadline,
                    onDismiss = { shouldShowDatePicker = false },
                    onDateSelected = {
                        onDeadlineChanged(it)
                        shouldShowDatePicker = false
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CreateBetStatementInputPreview() {
    PreviewScaffold {
        CreateBet(
            state = CreateBetScreenState(
                statement = "",
                deadline = LocalDate.now()
            )
        )
    }
}
