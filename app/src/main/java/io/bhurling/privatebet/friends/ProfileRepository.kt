package io.bhurling.privatebet.friends

import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.common.firestore.snapshots
import io.bhurling.privatebet.model.pojo.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) {
    fun updateProfile(
        displayName: String?,
        photoUrl: String?,
        email: String?
    ) {
        auth.currentUser?.uid?.let { uid ->
            store.collection("private_profiles").document(uid)
                .set(mapOf("email" to email))
            store.collection("public_profiles").document(uid)
                .set(mapOf("displayName" to displayName, "photoUrl" to photoUrl))
        }
    }

    fun byId(id: String): Flow<Profile> {
        return store.profiles.document(id).snapshots<FirestorePublicProfile>()
            .filterNotNull()
            .map(FirestorePublicProfile::toProfile)
    }

    fun observeAll(): Flow<List<Profile>> {
        return store.profiles.orderBy("displayName").snapshots<FirestorePublicProfile>()
            .map { firestoreProfiles ->
                firestoreProfiles.map(FirestorePublicProfile::toProfile)
            }
    }
}

private val FirebaseFirestore.profiles get() = collection("public_profiles")

@Keep
data class FirestorePublicProfile(
    @DocumentId val documentId: String = "",
    val displayName: String = "",
    val photoUrl: String? = null
)

fun FirestorePublicProfile.toProfile() = Profile(
    id = documentId,
    displayName = displayName,
    photoUrl = photoUrl
)
