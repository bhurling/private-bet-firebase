package io.bhurling.privatebet.friends

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import io.bhurling.privatebet.model.pojo.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val store: FirebaseFirestore
) {
    fun observeAll(): Flow<List<Profile>> {
        return store.profiles.orderBy("displayName").snapshots().map { snapshot ->
            snapshot.toObjects<FirestoreProfile>().map { it.toProfile() }
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
