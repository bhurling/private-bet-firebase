package io.bhurling.privatebet.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import org.koin.inject

class SignupActivity : AppCompatActivity() {

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                listOf(
                                        AuthUI.IdpConfig.GoogleBuilder().build()
                                )
                        )
                        .build(),
                REQUEST_CODE_AUTH
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_AUTH) {
            if (resultCode == Activity.RESULT_OK) {
                navigator.launchApp(this)
            }

            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val REQUEST_CODE_AUTH = 0
    }
}