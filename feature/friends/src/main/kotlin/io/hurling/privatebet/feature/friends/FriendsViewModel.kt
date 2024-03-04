package io.hurling.privatebet.feature.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.hurling.privatebet.core.domain.GetFriendsUseCase
import io.hurling.privatebet.core.domain.AcceptInvitationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class FriendsViewModel @Inject constructor(
    private val getFriends: GetFriendsUseCase,
    private val acceptInvitation: AcceptInvitationUseCase
) : ViewModel() {
    val state by lazy {
        getFriends()
            .map { friends ->
                FriendsScreenState.Success(items = friends)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FriendsScreenState.Loading
            )
    }

    fun acceptInvitation(id: String) {
        acceptInvitation.invoke(id)
    }
}
