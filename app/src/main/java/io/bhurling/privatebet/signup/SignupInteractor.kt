package io.bhurling.privatebet.signup

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SignupInteractor @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
) {

    fun updateProfile(
        displayName: String?,
        photoUrl: String?,
        email: String?
    ) {
        auth.currentUser?.uid?.let { uid ->
            store.privateProfile(uid).set(PrivateProfileData(email))
            store.publicProfile(uid).set(PublicProfileData(displayName, photoUrl))
        }
    }
}


private fun FirebaseFirestore.privateProfile(uid: String) =
    collection("private_profiles").document(uid)

private fun FirebaseFirestore.publicProfile(uid: String) =
    collection("public_profiles").document(uid)

private data class PrivateProfileData(
    val email: String?
)

private data class PublicProfileData(
    val displayName: String?,
    val photoUrl: String?
)
