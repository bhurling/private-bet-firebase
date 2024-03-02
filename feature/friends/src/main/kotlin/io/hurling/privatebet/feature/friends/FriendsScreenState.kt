package io.hurling.privatebet.feature.friends

data class FriendsScreenState(
    val items: List<Friend> = emptyList()
)

data class Friend(
    val id: String,
    val displayName: String,
    val photoUrl: String?,
    val isConfirmed: Boolean = false,
    val isIncoming: Boolean = false
)
