package io.bhurling.privatebet.signup

import com.google.firebase.database.DatabaseReference

class SignupInteractor constructor(
        val profile: DatabaseReference
) {

    fun updateProfile(
            displayName: String?,
            photoUrl: String?,
            email: String?
    ) {
        profile.child("private").setValue(PrivateProfileData(email))
        profile.child("public").setValue(PublicProfileData(displayName, photoUrl))
    }
}

private data class PrivateProfileData(
        val email: String?
)

private data class PublicProfileData(
        val displayName: String?,
        val photoUrl: String?
)
