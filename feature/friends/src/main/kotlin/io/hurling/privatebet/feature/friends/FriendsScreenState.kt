package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.domain.Friend

sealed interface FriendsScreenState {
    data object Loading: FriendsScreenState
    data class Success(val items: List<Friend>) : FriendsScreenState
}
