package io.bhurling.privatebet.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import io.bhurling.privatebet.R
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch

class SignupActivity : AppCompatActivity() {

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
                EntryPoint.Home.launch(this)

                SignupService.launch(this)
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