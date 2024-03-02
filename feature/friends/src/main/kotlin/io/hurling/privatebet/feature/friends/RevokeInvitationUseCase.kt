package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import javax.inject.Inject

internal class RevokeInvitationUseCase @Inject constructor(
    private val auth: Auth,
    private val linksRepository: LinksRepository
) {
    fun invoke(id: String) {
        auth.currentUserId?.let { uid ->
            invoke(uid, id)
        }
    }

    private fun invoke(fromId: String, toId: String) {
        linksRepository.removeOutgoingLink(fromId, toId)
    }
}
