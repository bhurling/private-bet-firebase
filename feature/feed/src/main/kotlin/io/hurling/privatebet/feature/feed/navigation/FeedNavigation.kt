package io.hurling.privatebet.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.hurling.privatebet.feature.feed.FeedScreen

const val FEED_ROUTE = "feed_route"

fun NavGraphBuilder.feedScreen() {
    composable(
        route = FEED_ROUTE,
    ) {
        FeedScreen()
    }
}
