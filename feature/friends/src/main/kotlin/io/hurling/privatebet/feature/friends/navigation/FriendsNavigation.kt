package io.hurling.privatebet.feature.friends.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.hurling.privatebet.feature.friends.FriendsScreen

const val FRIENDS_ROUTE = "friends_route"

fun NavGraphBuilder.friendsScreen() {
    composable(
        route = FRIENDS_ROUTE,
    ) {
        FriendsScreen()
    }
}