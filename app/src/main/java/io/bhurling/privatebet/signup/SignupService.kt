package io.bhurling.privatebet.signup

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.common.push.TokenRepository
import io.bhurling.privatebet.friends.ProfileRepository
import javax.inject.Inject

@AndroidEntryPoint
class SignupService : IntentService("SignupService") {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var tokenRepository: TokenRepository

    override fun onHandleIntent(intent: Intent?) {
        val data = auth.currentUser?.providerData?.first { it.providerId == "firebase" }

        data?.let {
            profileRepository.updateProfile(it.displayName, it.photoUrl.toString(), it.email)
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            token?.let {
                tokenRepository.addDeviceToken(it)
            }
        }
    }

    companion object {
        fun launch(context: Context) {
            context.startService(Intent(context, SignupService::class.java))
        }
    }
}