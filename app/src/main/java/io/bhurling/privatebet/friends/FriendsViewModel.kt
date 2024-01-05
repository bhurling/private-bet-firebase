package io.bhurling.privatebet.friends

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.ViewModelState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class FriendsViewModel @Inject constructor(
    private val invitationsRepository: InvitationsRepository
) : ViewModel() {

    val state = combine(incoming(), confirmed()) { incoming, confirmed ->
        incoming + confirmed
    }.map { invites ->
        invites.reversed().distinctBy { it.profile.id }.reversed()
    }.map { items ->
        FriendsState(items)
    }

    private fun incoming(): Flow<List<FriendsAdapterItem>> {
        return invitationsRepository.incoming()
            .map { incoming ->
                incoming.map { profile ->
                    FriendsAdapterItem(profile, isInvited = true)
                }
            }
    }

    private fun confirmed(): Flow<List<FriendsAdapterItem>> {
        return invitationsRepository.confirmed()
            .map { confirmed ->
                confirmed.map { profile ->
                    FriendsAdapterItem(profile, isConfirmed = true)
                }
            }
    }

    fun acceptInvitation(id: String) {
        invitationsRepository.accept(id)
    }
}

internal data class FriendsState(
    val items: List<FriendsAdapterItem>? = null
) : ViewModelState
