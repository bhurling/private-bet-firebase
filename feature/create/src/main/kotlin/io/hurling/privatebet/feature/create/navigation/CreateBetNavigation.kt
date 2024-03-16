package io.hurling.privatebet.feature.create.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.hurling.privatebet.feature.create.CreateBetScreen

private const val CREATE_BET_ROUTE = "create_bet_route"

fun NavController.navigateToCreateBetScreen() {
    navigate(CREATE_BET_ROUTE)
}

fun NavGraphBuilder.createBetScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = CREATE_BET_ROUTE
    ) {
        CreateBetScreen(
            onBackClick = onBackClick
        )
    }
}
