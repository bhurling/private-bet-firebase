package io.hurling.privatebet.feature.friends.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.hurling.privatebet.feature.friends.FriendsScreen
import io.hurling.privatebet.feature.friends.InviteScreen

const val FRIENDS_ROUTE = "friends_route"
const val INVITE_ROUTE = "invite_route"

fun NavController.navigateToInviteScreen() {
    navigate(INVITE_ROUTE)
}

fun NavGraphBuilder.friendsScreen(
    navController: NavController
) {
    composable(
        route = FRIENDS_ROUTE,
    ) {
        FriendsScreen(onConnectClick = navController::navigateToInviteScreen)
    }
    composable(
        route = INVITE_ROUTE
    ) {
        InviteScreen()
    }
}
