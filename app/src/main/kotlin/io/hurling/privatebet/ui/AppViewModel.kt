package io.hurling.privatebet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.auth.AuthState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    auth: Auth
) : ViewModel() {
    val state = auth.authState
        .map { authState ->
            AppState(
                authState = authState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppState(
                authState = AuthState.Unknown
            )
        )
}

data class AppState(
    val authState: AuthState
)
