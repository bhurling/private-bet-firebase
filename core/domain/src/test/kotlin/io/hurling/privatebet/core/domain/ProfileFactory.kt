package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.data.Profile
import java.util.UUID

object ProfileFactory {
    fun create() = Profile(
        id = UUID.randomUUID().toString(),
        displayName = "",
        photoUrl = null
    )
}
