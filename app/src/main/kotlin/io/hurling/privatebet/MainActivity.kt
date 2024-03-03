package io.hurling.privatebet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.hurling.privatebet.core.auth.Auth
import io.hurling.privatebet.core.design.Theme
import io.hurling.privatebet.signin.registerSignInLauncher
import io.hurling.privatebet.ui.App
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val launcher = registerSignInLauncher()

    @Inject
    lateinit var auth: Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                App(onLaunchSignIn = launcher::launch)
            }
        }
    }
}
