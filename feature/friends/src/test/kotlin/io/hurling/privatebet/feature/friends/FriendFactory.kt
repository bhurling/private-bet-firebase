package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.domain.Friend
import java.util.UUID

object FriendFactory {
    fun create() = Friend(
        id = UUID.randomUUID().toString(),
        displayName = "",
        photoUrl = null
    )
}