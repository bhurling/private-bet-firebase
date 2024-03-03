package io.hurling.privatebet.signin

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

fun ComponentActivity.registerSignInLauncher(onSuccess: () -> Unit): SignInLauncher {
    return SignInLauncher(
        innerLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
            if (result.resultCode == RESULT_OK) onSuccess()
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
