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
        val data = ProfileData(
                private = PrivateProfileData(email),
                public = PublicProfileData(displayName, photoUrl)
        )

        profile.setValue(data)
    }
}

private data class ProfileData(
        val private: PrivateProfileData,
        val public: PublicProfileData
)

private data class PrivateProfileData(
        val email: String?
)

private data class PublicProfileData(
        val displayName: String?,
        val photoUrl: String?
)
