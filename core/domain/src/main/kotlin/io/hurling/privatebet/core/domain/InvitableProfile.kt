package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.data.Profile

data class InvitableProfile(
    val profile: Profile,
    val isInvited: Boolean = false,
)