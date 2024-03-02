package io.hurling.privatebet.feature.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    getFriends: GetFriendsUseCase
) : ViewModel() {
    val state = getFriends()
        .map { friends ->
            FriendsScreenState(items = friends)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FriendsScreenState()
        )

    fun acceptInvitation(id: String) {
        TODO("Not yet implemented")
    }
}
