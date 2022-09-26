package com.hb.jetpack_compose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController


abstract class BaseFragment : Fragment() {
    //    参考setContent的实现
    fun createComposeView(contentView: @Composable () -> Unit): ComposeView {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                AppCompatTheme {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = !isSystemInDarkTheme()
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = useDarkIcons
                        )
                    }
                    contentView.invoke()
                }
            }
        }
        return composeView
    }
}