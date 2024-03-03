package io.hurling.privatebet.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

fun ComponentActivity.registerSignInLauncher(): SignInLauncher {
    return SignInLauncher(
        innerLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            /* Np-Op */
        }
    )
}

class SignInLauncher(
    private val innerLauncher: ActivityResultLauncher<Intent>
) {
    fun launch() {
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(AuthUI.IdpConfig.GoogleBuilder().build())
            )
            .build()

        innerLauncher.launch(intent)
    }
}