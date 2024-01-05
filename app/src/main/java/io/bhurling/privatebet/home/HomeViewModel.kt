package io.bhurling.privatebet.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bhurling.privatebet.friends.InvitationsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val invitationsRepository: InvitationsRepository
) : ViewModel() {

    val state = invitationsRepository.confirmed()
        .map { confirmed ->
            HomeState(
                isPrimaryActionVisible = confirmed.isNotEmpty()
            )
        }
}

internal data class HomeState(
    val isPrimaryActionVisible: Boolean = false
)
