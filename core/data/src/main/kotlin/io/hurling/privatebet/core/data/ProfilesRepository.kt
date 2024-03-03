package io.hurling.privatebet.core.data

import io.hurling.privatebet.core.network.NetworkProfilesDataSource
import io.hurling.privatebet.core.network.NetworkPublicProfile
import io.hurling.privatebet.core.network.NetworkPublicProfileUpdateParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProfilesRepository {
    fun profiles(): Flow<List<Profile>>
    fun profileById(id: String): Flow<Profile?>
    fun profilesByIds(ids: List<String>): Flow<List<Profile>>
    fun updateProfile(uid: String, displayName: String?, photoUrl: String?)
}

class DefaultProfilesRepository @Inject constructor(
    private val networkDataSource: NetworkProfilesDataSource
) : ProfilesRepository {
    override fun profiles() =
        networkDataSource.publicProfiles().map { networkPublicProfiles ->
            networkPublicProfiles.map(NetworkPublicProfile::asEntity)
        }

    override fun profileById(id: String) =
        networkDataSource.publicProfileById(id).map { it?.asEntity() }

    override fun profilesByIds(ids: List<String>) =
        networkDataSource.publicProfilesByIds(ids).map { networkPublicProfiles ->
            networkPublicProfiles.map(NetworkPublicProfile::asEntity)
        }

    override fun updateProfile(uid: String, displayName: String?, photoUrl: String?) {
        networkDataSource.updatePublicProfile(
            id = uid,
            params = NetworkPublicProfileUpdateParams(displayName, photoUrl)
        )
    }
}

fun NetworkPublicProfile.asEntity() = Profile(
    id = documentId,
    displayName = displayName,
    photoUrl = photoUrl
)

data class Profile(
    val id: String,
    val displayName: String,
    val photoUrl: String?
)
