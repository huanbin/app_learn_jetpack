package com.hb.jetpack_compose.ui

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.ui.detail.DetailScreen
import com.hb.jetpack_compose.ui.home.HomeScreen
import com.hb.jetpack_compose.ui.setting.SettingScreen
import com.hb.jetpack_compose.ui.topics.TopicScreen

@Composable
fun HbNavHost(controller: NavHostController, modifier: Modifier) {
    NavHost(navController = controller,
        startDestination = Screen.Home.route,
        modifier = modifier,
        builder = {

            composable(route = Screen.Home.route) {
                HomeScreen(onNavigate = {
                    controller.navigate(route = "${Screen.Detail.route}?url=$it")
                })
            }

            composable(route = Screen.Topic.route) {
                TopicScreen {
                    controller.navigate(
                        //这里有一个bug
                        // 传递uri中必须有http，否则ViewModel中无法获取参数值。但是导航中定义deepLink时uri可以设置为hb.com/articles?url={url}
                        NavDeepLinkRequest.Builder.fromUri(Uri.parse("http://hb.com/articles?url=${it}"))
                            .setAction("android.intent.action.ACTION_READ_ARTICLE")
                            .setMimeType("type/subtype").build()
                    )
                }
            }

            composable(route = Screen.Setting.route) {
                SettingScreen()
            }

            composable(
                route = "${Screen.Detail.route}?url={url}",
                arguments = listOf(navArgument(name = "url") {
                    type = NavType.StringType
                }),
                deepLinks = listOf(navDeepLink {
                    uriPattern = "http://hb.com/articles?url={url}"
                    action = "android.intent.action.ACTION_READ_ARTICLE"
                    mimeType = "type/subtype"
                })
            ) {
                DetailScreen {
                    controller.navigateUp()
                }
            }
        })
}


sealed class Screen(val route: String, @DrawableRes val drawableRes: Int) {
    object Home : Screen("home", R.drawable.ic_outline_home_24)
    object Topic : Screen("topic", R.drawable.ic_outline_topic_24)
    object Setting : Screen("setting", R.drawable.ic_outline_settings_24)
    object Detail : Screen("detail", -1)
}