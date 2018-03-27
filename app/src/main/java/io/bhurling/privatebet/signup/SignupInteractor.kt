package io.bhurling.privatebet.signup

import com.google.firebase.database.DatabaseReference

class SignupInteractor constructor(
        private val privateProfile: DatabaseReference,
        private val publicProfile: DatabaseReference
) {

    fun updateProfile(
            displayName: String?,
            photoUrl: String?,
            email: String?
    ) {
        privateProfile.setValue(PrivateProfileData(email))
        publicProfile.setValue(PublicProfileData(displayName, photoUrl))
    }
}

private data class PrivateProfileData(
        val email: String?
)

private data class PublicProfileData(
        val displayName: String?,
        val photoUrl: String?
)
