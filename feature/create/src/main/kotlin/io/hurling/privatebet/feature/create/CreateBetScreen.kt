package io.hurling.privatebet.feature.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.hurling.privatebet.core.design.PreviewScaffold
import io.hurling.privatebet.core.design.PrivateBetIcons
import io.hurling.privatebet.core.design.autoFocus
import io.hurling.privatebet.feature.create.ui.DeadlinePickerDialog
import io.hurling.privatebet.feature.create.ui.OutlinedDecoratedText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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
    onDeadlineChanged: (LocalDate?) -> Unit = {}
) {
    var shouldShowDatePicker by remember {
        mutableStateOf(false)
    }

    val deadlineString = state.deadline?.let {
        DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.US)
            .format(it)
    } ?: stringResource(id = R.string.create_bet_no_deadline)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)

    ) {
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
            OutlinedDecoratedText(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { shouldShowDatePicker = true },
                innerText = {
                    Text(modifier = Modifier.fillMaxWidth(), text = deadlineString)
                },
                label = {
                    Text(text = stringResource(R.string.create_bet_deadline_label))
                }
            )

            IconButton(
                enabled = state.deadline != null,
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { onDeadlineChanged(null) }
            ) {
                Icon(imageVector = PrivateBetIcons.Clear, contentDescription = null)
            }
        }

        if (shouldShowDatePicker) {
            DeadlinePickerDialog(
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

