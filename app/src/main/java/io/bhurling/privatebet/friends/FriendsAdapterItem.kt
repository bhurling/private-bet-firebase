package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Profile

data class FriendsAdapterItem(
    val profile: Profile,
    val isConfirmed: Boolean = false,
    val isInvited: Boolean = false
)