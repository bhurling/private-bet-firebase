package io.bhurling.privatebet.friends

data class FriendsAdapterItem(
        val id: String,
        val isConfirmed: Boolean = false,
        val isInvited: Boolean = false
)