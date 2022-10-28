package com.hb.jetpack_compose.ui.nav

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


val items = listOf(Screen.Home, Screen.Topic, Screen.Setting)

@Composable
fun HbNavigation(navHostController: NavHostController, modifier: Modifier) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    val showBottomNavigation by remember(currentDestination) {
        derivedStateOf { items.contains(items.find { it.route == currentDestination?.route }) }
    }

    if (showBottomNavigation) {

        BottomNavigation(modifier = modifier) {

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
}