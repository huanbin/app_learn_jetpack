package com.hb.jetpack_compose.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.appcompattheme.AppCompatTheme


open abstract class BaseFragment : Fragment() {

    @Composable
    abstract fun createView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                AppCompatTheme {
                    createView()
                }
            }
        }
        return composeView
    }
}