package io.bhurling.privatebet

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch
import javax.inject.Inject

@AndroidEntryPoint
class EntryActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

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
