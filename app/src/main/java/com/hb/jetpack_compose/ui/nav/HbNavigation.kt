package com.hb.jetpack_compose.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hb.jetpack_compose.ui.Screen


val items = listOf(Screen.Home, Screen.Topic, Screen.Setting)

@Composable
fun HbNavigation(navHostController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()

        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            BottomNavigationItem(icon = {
                Icon(
                    painter = painterResource(id = item.drawableRes),
                    contentDescription = item.route
                )
            },
                label = {
                    Text(text = item.route)
                },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                })
        }
    }
}