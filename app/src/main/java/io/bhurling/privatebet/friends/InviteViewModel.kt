package io.bhurling.privatebet.friends

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.arch.ViewModelState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class InviteViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val invitationsRepository: InvitationsRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val state = combine(
        profileRepository.observeAll(),
        invitationsRepository.incoming(),
        invitationsRepository.outgoing()
    ) { persons, incoming, outgoing ->
        persons.filter {
            it.id != auth.currentUser?.uid
        }.map {
            InviteAdapterItem(
                profile = it,
                isIncoming = incoming.contains(it),
                isSent = outgoing.contains(it)
            )
        }.filterNot { it.isIncoming }
    }.map { items ->
        InviteState(items = items)
    }

    fun sendInvite(id: String) {
        invitationsRepository.invite(id)
    }

    fun revokeInvite(id: String) {
        invitationsRepository.revoke(id)
    }
}

internal data class InviteState(
    val items: List<InviteAdapterItem> = emptyList()
) : ViewModelState