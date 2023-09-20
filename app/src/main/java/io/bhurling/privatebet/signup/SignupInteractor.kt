package io.bhurling.privatebet.signup

import com.google.firebase.firestore.DocumentReference
import io.bhurling.privatebet.MePrivateDocument
import io.bhurling.privatebet.MePublicDocument
import javax.inject.Inject

class SignupInteractor @Inject constructor(
    @MePrivateDocument private val privateProfile: DocumentReference,
    @MePublicDocument private val publicProfile: DocumentReference
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
