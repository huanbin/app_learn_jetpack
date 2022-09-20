package com.hb.jetpack_compose.ui.home


import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import androidx.paging.compose.collectAsLazyPagingItems
import com.hb.jetpack_compose.ui.BaseFragment
import com.hb.jetpack_compose.ui.compose.SwiperefreshLayout

class HomeFragment : BaseFragment() {

    @Composable
    override fun createView() {
        //确保viewmodel的生命周期（保存lazyListState）
        val viewModel by activityViewModels<HomeViewModel>()
        HomeScreen(viewModel)
    }
}


@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val lazyListState = viewModel.lazyListState
    val pager = viewModel.pager.collectAsLazyPagingItems()
    SwiperefreshLayout(lazyListState, pager)
}