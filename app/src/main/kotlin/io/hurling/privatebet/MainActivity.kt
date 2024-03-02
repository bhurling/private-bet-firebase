package io.hurling.privatebet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import io.hurling.privatebet.core.design.Theme
import io.hurling.privatebet.feature.friends.FriendsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    FriendsScreen()
}