package io.bhurling.privatebet.signup

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import io.bhurling.privatebet.common.push.TokenInteractor
import org.koin.inject

class SignupService : IntentService("SignupService") {

    private val currentUser: FirebaseUser by inject()
    private val signupInteractor: SignupInteractor by inject()
    private val tokenInteractor: TokenInteractor by inject()

    override fun onHandleIntent(intent: Intent?) {
        val data = currentUser.providerData.first { it.providerId == "firebase" }

        data?.let {
            signupInteractor.updateProfile(it.displayName, it.photoUrl.toString(), it.email)
        }

        FirebaseInstanceId.getInstance().token?.let { token ->
            tokenInteractor.addDeviceToken(token)
        }
    }

    companion object {
        fun launch(context: Context) {
            context.startService(Intent(context, SignupService::class.java))
        }
    }
}