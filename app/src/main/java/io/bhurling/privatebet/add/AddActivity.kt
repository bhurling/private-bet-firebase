package io.bhurling.privatebet.add

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.common.ui.Theme

@AndroidEntryPoint
class AddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setContent {
                AddBetScreen()
            }
        })
    }
}

@Composable
fun AddBetScreen() {
    val viewModel: AddBetViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    AddBet(
        state = state,
        onStatementChange = {
            viewModel.offer(AddAction.StatementChanged(it))
        },
        onDeadlineClear = {
            viewModel.offer(AddAction.DeadlineCleared)
        },
        onDeadlineClick = {
            viewModel.offer(AddAction.DeadlineClicked)
        }
    )
}

@Composable
fun AddBet(
    state: AddViewState,
    onStatementChange: (String) -> Unit = {},
    onDeadlineClick: (Long?) -> Unit = {},
    onDeadlineClear: () -> Unit = {}
) {
    Theme {
        when (state.step) {
            AddViewState.Step.STATEMENT -> StatementStep(
                state,
                onStatementChange = onStatementChange,
                onDeadlineClick = onDeadlineClick,
                onDeadlineClear = onDeadlineClear
            )

            AddViewState.Step.STAKE -> StakeStep(state)
            AddViewState.Step.OPPONENT -> OpponentStep(state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatementStep(
    state: AddViewState,
    onStatementChange: (String) -> Unit = {},
    onDeadlineClick: (Long?) -> Unit = {},
    onDeadlineClear: () -> Unit = {}
) {
    val context = LocalContext.current
    val deadline = remember(state.deadline) {
        state.deadline?.let { long ->
            DateFormat.getMediumDateFormat(context).format(long)
        }
    }

    Column(modifier = Modifier.padding(horizontal = 48.dp)) {
        Box(modifier = Modifier.height(56.dp), contentAlignment = Alignment.CenterStart) {
            Text(
                text = stringResource(id = R.string.add_bet_statement_label),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.secondary,
                backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = .4f)
            )
        ) {
            val interactionSource = remember { MutableInteractionSource() }

            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.statement,
                onValueChange = onStatementChange,
                textStyle = LocalTextStyle.current.merge(MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium)),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = state.statement,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        contentPadding = PaddingValues(vertical = 8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.deadline_label),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1 * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = Color.Black,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .clickable {
                onDeadlineClick(state.deadline)
            }) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = deadline ?: stringResource(id = R.string.no_deadline),
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onDeadlineClear() },
                imageVector = Icons.Default.Clear, contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StakeStep(state: AddViewState) {

}

@Composable
fun OpponentStep(state: AddViewState) {

}

@Preview
@Composable
fun StatementStepPreview() {
    Theme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            StatementStep(
                state = AddViewState(
                    statement = "Haters gonna hate it so much",
                    deadline = 1234
                )
            )
        }
    }
}
