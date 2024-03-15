package io.hurling.privatebet.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.hurling.privatebet.feature.feed.FeedScreen

private const val FEED_ROUTE = "feed_route"
const val FEED_TAB_ROUTE = "${FEED_ROUTE}_tab"

fun NavGraphBuilder.feedScreen(
    onCreateBetClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = FEED_TAB_ROUTE,
        startDestination = FEED_ROUTE
    ) {
        composable(
            route = FEED_ROUTE,
        ) {
            FeedScreen(onCreateBetClick = onCreateBetClick)
        }
        nestedGraphs()
    }
}
