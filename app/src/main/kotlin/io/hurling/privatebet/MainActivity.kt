package io.hurling.privatebet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.hurling.privatebet.core.design.Theme
import io.hurling.privatebet.feature.friends.FriendsScreen
import io.hurling.privatebet.feature.friends.navigation.FRIENDS_ROUTE
import io.hurling.privatebet.feature.friends.navigation.friendsScreen

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
    val navController = rememberNavController()
    Scaffold { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            NavHost(
                navController = navController,
                startDestination = FRIENDS_ROUTE,
                modifier = Modifier
            ) {
                friendsScreen(
                    navController = navController
                )
            }
        }
    }
}
