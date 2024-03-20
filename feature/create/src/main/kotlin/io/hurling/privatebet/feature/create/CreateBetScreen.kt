package io.hurling.privatebet.feature.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.hurling.privatebet.core.data.Profile
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
internal fun CreateBetScreen(
    onBackClick: () -> Unit
) {
    val viewModel: CreateBetViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateBet(
        state = state,
        onBackClick = if (state.shouldInterceptBackPress) {
            viewModel::onBackClick
        } else {
            onBackClick
        },
        onNextClick = viewModel::onNextClick,
        onStatementChanged = viewModel::onStatementChanged,
        onDeadlineChanged = viewModel::onDeadlineChanged,
        onStakeChanged = viewModel::onStakeChanged
    )

    BackHandler(
        enabled = state.shouldInterceptBackPress,
        onBack = viewModel::onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateBet(
    state: CreateBetScreenState,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onStatementChanged: (String) -> Unit = {},
    onDeadlineChanged: (LocalDate?) -> Unit = {},
    onStakeChanged: (String) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = PrivateBetIcons.ArrowBack, contentDescription = null)
                }
            },
            title = {
                Text(text = stringResource(R.string.create_bet_screen_title))
            },
            actions = {
                TextButton(
                    enabled = state.isNextButtonEnabled,
                    onClick = onNextClick
                ) {
                    Text(text = stringResource(R.string.create_bet_button_next))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = PrivateBetIcons.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        )

        when (state.step) {
            CreateBetStep.Statement -> CreateBetStatementStep(
                state = state,
                onStatementChanged = onStatementChanged,
                onDeadlineChanged = onDeadlineChanged
            )

            CreateBetStep.Stake -> CreateBetStakeStep(
                state = state,
                onStakeChanged = onStakeChanged
            )

            CreateBetStep.Opponent -> CreateBetOpponentStep(
                state = state
            )
        }
    }
}

@Composable
private fun CreateBetStatementStep(
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

    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 32.dp)) {
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

@Composable
private fun CreateBetStakeStep(
    state: CreateBetScreenState,
    onStakeChanged: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 32.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .autoFocus(),
            value = state.stake,
            onValueChange = onStakeChanged,
            label = {
                Text(text = stringResource(R.string.create_bet_stake_label))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            )
        )
    }
}

@Composable
private fun CreateBetOpponentStep(
    state: CreateBetScreenState
) {
    Column(modifier = Modifier.padding(vertical = 24.dp)) {
        LazyColumn {
            items(state.friends, Profile::id) { profile ->
                Row(
                    modifier = Modifier
                        .clickable {
                            // TODO onOpponentSelected
                        }
                        .padding(horizontal = 16.dp)
                        .height(72.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        model = profile.photoUrl,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        text = profile.displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
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
                step = CreateBetStep.Statement,
                statement = "Summer is gonna come",
                stake = "",
                deadline = LocalDate.now(),
                friends = emptyList()
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun CreateBetOpponentPreview() {
    PreviewScaffold {
        CreateBet(
            state = CreateBetScreenState(
                step = CreateBetStep.Opponent,
                statement = "Summer is gonna come",
                stake = "",
                deadline = LocalDate.now(),
                friends = listOf(
                    Profile(
                        id = "1",
                        displayName = "Bill Gates",
                        photoUrl = null
                    ),
                    Profile(
                        id = "2",
                        displayName = "John Doe",
                        photoUrl = null
                    )
                )
            )
        )
    }
}
