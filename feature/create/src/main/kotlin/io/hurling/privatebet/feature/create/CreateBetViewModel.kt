package io.hurling.privatebet.feature.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class CreateBetViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = combine(
        savedStateHandle.getStateFlow("statement", ""),
        savedStateHandle.getStateFlow<LocalDate?>("deadline", null)
    ) { statement, deadline ->
        CreateBetScreenState(
            statement = statement,
            deadline = deadline
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CreateBetScreenState(
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

    }
}

internal data class CreateBetScreenState(
    val statement: String,
    val deadline: LocalDate?
) {
    val isNextButtonEnabled = statement.isNotBlank()
}
