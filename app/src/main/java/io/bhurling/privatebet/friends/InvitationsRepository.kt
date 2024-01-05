package io.bhurling.privatebet.friends

import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.common.firestore.snapshots
import io.bhurling.privatebet.model.pojo.Profile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InvitationsRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
) {

    private val myLinks by lazy {
        store.collection("links").document(auth.currentUser?.uid ?: "")
    }

    fun incoming(): Flow<List<Profile>> {
        return myLinks.collection("incoming")
            .whereEqualTo("linked", true)
            .snapshots<FirestoreLink>()
            .mapToPublicProfiles()
            .map { firestoreProfiles -> firestoreProfiles.map(FirestoreProfile::toProfile) }
            .distinctUntilChanged()
    }

    fun outgoing(): Flow<List<Profile>> =
        myLinks.collection("outgoing")
            .whereEqualTo("linked", true)
            .snapshots<FirestoreLink>()
            .mapToPublicProfiles()
            .map { firestoreProfiles -> firestoreProfiles.map(FirestoreProfile::toProfile) }
            .distinctUntilChanged()

    fun confirmed(): Flow<List<Profile>> = myLinks.collection("confirmed")
        .whereEqualTo("linked", true)
        .snapshots<FirestoreLink>()
        .mapToPublicProfiles()
        .map { firestoreProfiles -> firestoreProfiles.map(FirestoreProfile::toProfile) }
        .distinctUntilChanged()

    fun invite(id: String) {
        myLinks.collection("outgoing").document(id).set(mapOf("linked" to true))
    }

    fun revoke(id: String) {
        myLinks.collection("outgoing").document(id).delete()
    }

    fun accept(id: String) {
        myLinks.collection("confirmed").document(id).set(mapOf("linked" to true))
    }

    fun decline(id: String) {
        myLinks.collection("incoming").document(id).delete()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<List<FirestoreLink>>.mapToPublicProfiles(): Flow<List<FirestoreProfile>> {
        return flatMapLatest { links ->
            if (links.isEmpty()) {
                flowOf(emptyList())
            } else {
                store.collection("public_profiles")
                    .whereIn(FieldPath.documentId(), links.map(FirestoreLink::documentId))
                    .snapshots()
            }
        }
    }
}

@Keep
data class FirestoreLink(
    @DocumentId val documentId: String = "",
    val linked: Boolean = false
)
