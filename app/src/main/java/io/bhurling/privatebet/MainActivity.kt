package io.bhurling.privatebet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import io.bhurling.privatebet.feed.FeedActivity
import org.koin.inject

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (auth.currentUser == null) {
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
            } else {
                startActivity(Intent(this, FeedActivity::class.java))
                finish()
            }
        }
    }

    companion object {

        private val REQUEST_CODE_AUTH = 0
    }
}
