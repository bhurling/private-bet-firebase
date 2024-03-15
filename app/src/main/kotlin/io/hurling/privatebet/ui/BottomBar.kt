package io.hurling.privatebet.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import io.hurling.privatebet.core.design.PrivateBetIcons
import io.hurling.privatebet.feature.feed.navigation.FEED_TAB_ROUTE
import io.hurling.privatebet.feature.friends.navigation.FRIENDS_TAB_ROUTE

@Composable
fun BottomBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        TopLevelDestination.entries.forEach { topLevelDestination ->
            NavigationBarItem(
                selected = topLevelDestination.isParentOf(currentDestination),
                onClick = {
                    navController.navigate(topLevelDestination.route, navOptions {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    })
                },
                icon = {
                    Icon(
                        imageVector = topLevelDestination.icon,
                        contentDescription = null
                    )
                })
        }
    }
}

enum class TopLevelDestination(
    val icon: ImageVector,
    val route: String
) {
    Feed(
        icon = PrivateBetIcons.List,
        route = FEED_TAB_ROUTE
    ),
    Friends(
        icon = PrivateBetIcons.Group,
        route = FRIENDS_TAB_ROUTE
    )
}

fun TopLevelDestination.isParentOf(currentDestination: NavDestination?): Boolean {
    return currentDestination?.hierarchy?.any { destinationInHierarchy ->
        destinationInHierarchy.route?.startsWith(route) ?: false
    } ?: false
}
