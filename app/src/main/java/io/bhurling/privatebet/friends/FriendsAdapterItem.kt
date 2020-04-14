package io.bhurling.privatebet.friends

import io.bhurling.privatebet.model.pojo.Person

data class FriendsAdapterItem(
        val person: Person,
        val isConfirmed: Boolean = false,
        val isInvited: Boolean = false
)