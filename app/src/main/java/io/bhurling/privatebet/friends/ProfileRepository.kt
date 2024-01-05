package io.bhurling.privatebet.friends

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.common.firestore.snapshots
import io.bhurling.privatebet.model.pojo.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val store: FirebaseFirestore
) {

    fun byId(id: String): Flow<Profile> {
        return store.profiles.document(id).snapshots<FirestoreProfile>()
            .filterNotNull()
            .map(FirestoreProfile::toProfile)
    }

    fun observeAll(): Flow<List<Profile>> {
        return store.profiles.orderBy("displayName").snapshots<FirestoreProfile>()
            .map { firestoreProfiles ->
                firestoreProfiles.map(FirestoreProfile::toProfile)
            }
    }
}

private val FirebaseFirestore.profiles get() = collection("public_profiles")

@Keep
data class FirestoreProfile(
    @DocumentId val documentId: String = "",
    val displayName: String = "",
    val photoUrl: String? = null
)

fun FirestoreProfile.toProfile() = Profile(
    id = documentId,
    displayName = displayName,
    photoUrl = photoUrl
)
