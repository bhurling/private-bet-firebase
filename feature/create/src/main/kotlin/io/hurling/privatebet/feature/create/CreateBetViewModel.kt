package io.hurling.privatebet.feature.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.lang.IllegalStateException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class CreateBetViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentStep = MutableStateFlow(CreateBetStep.Statement)

    val state = combine(
        currentStep,
        savedStateHandle.getStateFlow("statement", ""),
        savedStateHandle.getStateFlow<LocalDate?>("deadline", null),
    ) { currentStep, statement, deadline ->
        CreateBetScreenState(
            step = currentStep,
            statement = statement,
            deadline = deadline
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CreateBetScreenState(
            step = CreateBetStep.Statement,
            statement = "",
            deadline = null
        )
    )

    fun onStatementChanged(statement: String) {
        savedStateHandle["statement"] = statement
    }

    fun onDeadlineChanged(deadline: LocalDate?) {
        savedStateHandle["deadline"] = deadline
    }

    fun onNextClick() {
        currentStep.value = currentStep.value.next()
    }

    fun onBackClick() {
        currentStep.value = currentStep.value.previous()
    }
}

internal data class CreateBetScreenState(
    val step: CreateBetStep,
    val statement: String,
    val deadline: LocalDate?
) {
    val shouldInterceptBackPress get() = step != CreateBetStep.Statement
    val isNextButtonEnabled
        get() =
            step == CreateBetStep.Statement && statement.isNotBlank()
}

internal enum class CreateBetStep {
    Statement,
    Stake,
    Opponent
}

internal fun CreateBetStep.next() = when (this) {
    CreateBetStep.Statement -> CreateBetStep.Stake
    CreateBetStep.Stake -> CreateBetStep.Opponent
    CreateBetStep.Opponent -> throw IllegalStateException("Can't go forward from Opponent step")
}

internal fun CreateBetStep.previous() = when (this) {
    CreateBetStep.Statement -> throw IllegalStateException("Can't go back from Statement step")
    CreateBetStep.Stake -> CreateBetStep.Statement
    CreateBetStep.Opponent -> CreateBetStep.Stake
}
