package io.hurling.privatebet.core.network

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkDataSource : NetworkProfilesDataSource, NetworkLinksDataSource

interface NetworkProfilesDataSource {
    fun publicProfiles(): Flow<List<NetworkPublicProfile>>
    fun publicProfilesByIds(ids: List<String>): Flow<List<NetworkPublicProfile>>
    fun publicProfileById(id: String): Flow<NetworkPublicProfile?>
    fun updatePublicProfile(id: String, params: NetworkPublicProfileUpdateParams)
}

interface NetworkLinksDataSource {
    fun incomingForId(id: String): Flow<List<NetworkLink>>
    fun outgoingForId(id: String): Flow<List<NetworkLink>>
    fun confirmedForId(id: String): Flow<List<NetworkLink>>
}

@Singleton
internal class FirestoreDataSource @Inject constructor(
    private val store: FirebaseFirestore
) : NetworkDataSource {
    override fun publicProfiles(): Flow<List<NetworkPublicProfile>> {
        return store.collection("public_profiles")
            .snapshots<NetworkPublicProfile>()
    }

    override fun publicProfilesByIds(ids: List<String>): Flow<List<NetworkPublicProfile>> {
        return if (ids.isEmpty()) {
            flowOf(emptyList())
        } else {
            store.collection("public_profiles")
                .whereIn(FieldPath.documentId(), ids)
                .snapshots<NetworkPublicProfile>()
        }
    }

    override fun publicProfileById(id: String): Flow<NetworkPublicProfile?> {
        return store.collection("public_profiles")
            .document(id)
            .snapshots<NetworkPublicProfile>()
    }

    override fun updatePublicProfile(id: String, params: NetworkPublicProfileUpdateParams) {
        store.collection("public_profiles")
            .document(id)
            .update(params.asMap)
    }

    override fun incomingForId(id: String): Flow<List<NetworkLink>> {
        return store.collection("links").document(id)
            .collection("incoming")
            .whereEqualTo("linked", true)
            .snapshots()
    }

    override fun outgoingForId(id: String): Flow<List<NetworkLink>> {
        return store.collection("links").document(id)
            .collection("outgoing")
            .whereEqualTo("linked", true)
            .snapshots()
    }

    override fun confirmedForId(id: String): Flow<List<NetworkLink>> {
        return store.collection("links").document(id)
            .collection("confirmed")
            .whereEqualTo("linked", true)
            .snapshots()
    }

}

@Keep
data class NetworkPublicProfile(
    @DocumentId val documentId: String = "",
    val displayName: String = "",
    val photoUrl: String? = null
)

@Keep
data class NetworkPublicProfileUpdateParams(
    val displayName: String? = null,
    val photoUrl: String? = null
) {
    val asMap
        get() = buildMap {
            displayName?.let { put("displayName", it) }
            photoUrl?.let { put("photoUrl", it) }
        }
}

@Keep
data class NetworkLink(
    @DocumentId val documentId: String = "",
    val linked: Boolean = false
)