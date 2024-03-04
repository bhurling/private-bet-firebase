package io.hurling.privatebet.core.domain

import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.data.LinksRepository
import io.hurling.privatebet.core.data.ProfilesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val auth: Auth,
    private val profilesRepository: ProfilesRepository,
    private val linksRepository: LinksRepository
) {
    operator fun invoke(): Flow<List<Friend>> {
        return auth.currentUserId?.let { uid ->
            invoke(uid)
        } ?: emptyFlow()
    }

    private fun invoke(uid: String): Flow<List<Friend>> {
        return combine(confirmedFriends(uid), incomingFriends(uid)) { confirmed, incoming ->
            (confirmed + incoming).distinctBy { it.id }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun incomingFriends(id: String): Flow<List<Friend>> {
        return linksRepository.incoming(id)
            .flatMapLatest { incomingLinks ->
                profilesRepository.profilesByIds(incomingLinks)
            }.map { profiles ->
                profiles.map { profile ->
                    Friend(
                        id = profile.id,
                        displayName = profile.displayName,
                        photoUrl = profile.photoUrl,
                        isIncoming = true
                    )
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun confirmedFriends(id: String): Flow<List<Friend>> {
        return linksRepository.confirmed(id)
            .flatMapLatest { confirmedLinks ->
                profilesRepository.profilesByIds(confirmedLinks)
            }.map { profiles ->
                profiles.map { profile ->
                    Friend(
                        id = profile.id,
                        displayName = profile.displayName,
                        photoUrl = profile.photoUrl,
                        isConfirmed = true
                    )
                }
            }
    }
}