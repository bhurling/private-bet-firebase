package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Person

data class InviteAdapterItem(
        val person: Person,
        val isSent: Boolean,
        val isIncoming: Boolean
)