package com.hb.jetpack_compose.ui.home


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.hb.jetpack_compose.ui.BaseFragment

class HomeFragment : BaseFragment() {

    @Composable
    override fun createView() {
        val viewModel by viewModels<HomeViewModel>()
        viewModel.text.observe(this){
            println(it)
        }
        HomeScreen()
    }
}


@Composable
fun HomeScreen() {
    Text("HELLO WORLD!")
}