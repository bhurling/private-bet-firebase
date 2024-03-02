package io.hurling.privatebet.feature.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class InviteViewModel @Inject constructor(
    getInvitableProfiles: GetInvitableProfilesUseCase,
    private val sendInvitation: SendInvitationUseCase,
    private val revokeInvitation: RevokeInvitationUseCase
) : ViewModel() {

    val state = getInvitableProfiles()
        .map { profiles ->
            InviteScreenState.Success(
                items = profiles.sortedBy { it.profile.displayName }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InviteScreenState.Loading
        )

    fun sendInvitation(id: String) {
        sendInvitation.invoke(id)
    }

    fun revokeInvitation(id: String) {
        revokeInvitation.invoke(id)
    }
}
