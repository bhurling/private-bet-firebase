package io.hurling.privatebet.core.domain

data class Friend(
    val id: String,
    val displayName: String,
    val photoUrl: String?,
    val isConfirmed: Boolean = false,
    val isIncoming: Boolean = false
)