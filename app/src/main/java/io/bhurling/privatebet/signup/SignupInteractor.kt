package io.bhurling.privatebet.signup

import com.google.firebase.firestore.DocumentReference

class SignupInteractor constructor(
        private val privateProfile: DocumentReference,
        private val publicProfile: DocumentReference
) {

    fun updateProfile(
            displayName: String?,
            photoUrl: String?,
            email: String?
    ) {
        privateProfile.set(PrivateProfileData(email))
        publicProfile.set(PublicProfileData(displayName, photoUrl))
    }
}

private data class PrivateProfileData(
        val email: String?
)

private data class PublicProfileData(
        val displayName: String?,
        val photoUrl: String?
)
