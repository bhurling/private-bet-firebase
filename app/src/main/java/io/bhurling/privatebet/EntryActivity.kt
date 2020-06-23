package io.bhurling.privatebet

import android.app.Activity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch
import org.koin.android.ext.android.inject

class EntryActivity : Activity() {

    private val auth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (auth.currentUser == null) {
                EntryPoint.Signup.launch(this)
            } else {
                EntryPoint.Home.launch(this)
            }
        }

        finish()
    }
}
