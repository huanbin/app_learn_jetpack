package com.hb.jetpack_compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.hb.jetpack_compose.theme.JetpackComposeTheme
import com.hb.jetpack_compose.ui.nav.HbNavHost
import com.hb.jetpack_compose.ui.nav.HbNavigation

const val BottomNavigationViewHeight = 64

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JetpackComposeTheme {
                val navController = rememberNavController()
                Scaffold(bottomBar = {
                    HbNavigation(
                        navHostController = navController,
                        modifier = Modifier.navigationBarsPadding()
                    )
                }) {
                    HbNavHost(controller = navController, modifier = Modifier.padding(it))
                }
            }
        }
    }
}
