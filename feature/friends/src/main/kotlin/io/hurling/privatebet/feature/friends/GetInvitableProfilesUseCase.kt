package io.hurling.privatebet.feature.friends

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import io.hurling.privatebet.core.data.Profile
import io.hurling.privatebet.core.data.ProfilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

internal class GetInvitableProfilesUseCase @Inject constructor(
    private val auth: Auth,
    private val profilesRepository: ProfilesRepository,
    private val linksRepository: LinksRepository
) {
    operator fun invoke(): Flow<List<InvitableProfile>> {
        return auth.currentUserId?.let { uid ->
            invoke(uid)
        } ?: emptyFlow()
    }

    private fun invoke(uid: String): Flow<List<InvitableProfile>> {
        return combine(
            profilesRepository.profiles(),
            linksRepository.outgoing(uid)
        ) { profiles, outgoingLinks ->
            profiles.map { profile ->
                InvitableProfile(
                    profile = profile,
                    isInvited = outgoingLinks.contains(profile.id)
                )
            }.filterNot { it.profile.id == uid }
        }
    }
}

internal data class InvitableProfile(
    val profile: Profile,
    val isInvited: Boolean = false,
)
