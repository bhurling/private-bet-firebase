package io.bhurling.privatebet.signup

import android.app.IntentService
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import org.koin.inject

class SignupService : IntentService("SignupService") {

    val currentUser: FirebaseUser by inject()
    val interactor: SignupInteractor by inject()

    override fun onHandleIntent(intent: Intent?) {
        val data = currentUser.providerData.first { it.providerId == "firebase" }

        data?.let {
            interactor.updateProfile(it.displayName, it.photoUrl.toString(), it.email)
        }
    }
}