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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.hurling.privatebet.core.auth.AuthState
import io.hurling.privatebet.feature.create.navigation.createBetScreen
import io.hurling.privatebet.feature.create.navigation.navigateToCreateBetScreen
import io.hurling.privatebet.feature.feed.navigation.FEED_TAB_ROUTE
import io.hurling.privatebet.feature.feed.navigation.feedScreen
import io.hurling.privatebet.feature.friends.navigation.friendsScreen

@Composable
fun App(onLaunchSignIn: () -> Unit) {
    val navController = rememberNavController()

    val viewModel: AppViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.authState) {
        if (state.authState is AuthState.NotAuthenticated) {
            onLaunchSignIn()
        }
    }

    Scaffold(
        bottomBar = {
            if (state.shouldShowBottomBar) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            when (state.authState) {
                is AuthState.Authenticated -> AuthenticatedApp(navController)
                is AuthState.NotAuthenticated -> SplashScreen()
                is AuthState.Unknown -> {
                    /* */
                }
            }
        }
    }
}

@Composable
fun AuthenticatedApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = FEED_TAB_ROUTE,
        modifier = Modifier
    ) {
        feedScreen(
            onCreateBetClick = navController::navigateToCreateBetScreen,
            nestedGraphs = {
                createBetScreen()
            }
        )
        friendsScreen(
            navController = navController
        )
    }
}
