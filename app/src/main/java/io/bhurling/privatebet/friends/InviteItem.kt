package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Profile

data class InviteItem(
    val profile: Profile,
    val isSent: Boolean,
    val isIncoming: Boolean
)