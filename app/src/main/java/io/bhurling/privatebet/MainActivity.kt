package io.bhurling.privatebet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.inject

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by inject()
    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (auth.currentUser == null) {
                navigator.launchSignupFlow(this)
            } else {
                navigator.launchApp(this)
            }
        }

        finish()
    }
}
