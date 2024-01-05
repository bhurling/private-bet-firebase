package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Profile

data class InviteAdapterItem(
    val profile: Profile,
    val isSent: Boolean,
    val isIncoming: Boolean
)