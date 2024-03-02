package io.hurling.privatebet.feature.friends

sealed interface FriendsScreenState {
    data object Loading: FriendsScreenState
    data class Success(val items: List<Friend>) : FriendsScreenState
}

data class Friend(
    val id: String,
    val displayName: String,
    val photoUrl: String?,
    val isConfirmed: Boolean = false,
    val isIncoming: Boolean = false
)
