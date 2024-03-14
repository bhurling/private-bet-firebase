package io.hurling.privatebet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.hurling.privatebet.core.auth.AuthState
import io.hurling.privatebet.feature.friends.navigation.FRIENDS_ROUTE
import io.hurling.privatebet.feature.friends.navigation.friendsScreen

@Composable
fun App(onLaunchSignIn: () -> Unit) {
    val viewModel: AppViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.authState) {
        if (state.authState is AuthState.NotAuthenticated) {
            onLaunchSignIn()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            when (state.authState) {
                is AuthState.Authenticated -> AuthenticatedApp()
                is AuthState.NotAuthenticated -> SplashScreen()
                is AuthState.Unknown -> {
                    /* */
                }
            }
        }
    }
}

@Composable
fun AuthenticatedApp() {
    val navController = rememberNavController()

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
