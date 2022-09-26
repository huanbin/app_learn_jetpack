package com.hb.jetpack_compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.appcompattheme.AppCompatTheme


abstract class BaseFragment : Fragment() {
    //    参考setContent的实现
    fun createComposeView(contentView: @Composable () -> Unit): ComposeView {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                AppCompatTheme {
                    contentView.invoke()
                }
            }
        }
        return composeView
    }
}