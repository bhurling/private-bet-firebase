package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Profile

data class FriendsItem(
    val profile: Profile,
    val isConfirmed: Boolean = false,
    val isIncoming: Boolean = false
)