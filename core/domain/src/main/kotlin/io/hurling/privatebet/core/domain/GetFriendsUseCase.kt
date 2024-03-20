package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import io.hurling.privatebet.core.data.Profile
import io.hurling.privatebet.core.data.ProfilesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val auth: Auth,
    private val profilesRepository: ProfilesRepository,
    private val linksRepository: LinksRepository
) {

    operator fun invoke(): Flow<List<Profile>> {
        return auth.currentUserId?.let { uid ->
            invoke(uid)
        } ?: emptyFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun invoke(uid: String): Flow<List<Profile>> {
        return linksRepository.incoming(uid)
            .flatMapLatest { incomingLinks ->
                profilesRepository.profilesByIds(incomingLinks)
            }
    }
}
