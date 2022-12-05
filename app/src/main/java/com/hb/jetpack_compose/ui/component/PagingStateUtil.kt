package com.hb.jetpack_compose.ui.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.hb.jetpack_compose.R

class PagingStateUtil {

    //是否显示无数据的布局
    var showNullScreen = false

    /**
     * 统一管理Paging数据状态的方法
     * 错误处理、加载中的显示方式
     */
    @Composable
    fun <T : Any> pagingStateUtil(
        //paging数据
        pagingData: LazyPagingItems<T>,
        //刷新状态
        refreshState: SwipeRefreshState,
        viewModel: ViewModel,
        content: @Composable () -> Unit,
    ) {

        when (pagingData.loadState.refresh) {
            //未加载且未观察到错误
            is LoadState.NotLoading -> NotLoading(refreshState, viewModel) {
                //允许显示无数据布局 这里通常是第一次获取数据
                when (pagingData.itemCount) {
                    0 -> {
                        //第一次进入允许显示空布局
                        if (!showNullScreen) showNullScreen = true
                        //显示无数据布局
                        else
                            ErrorComposable(stringResource(id = R.string.empty_content)) {
                                pagingData.refresh()
                            }
                    }
                    else -> content()
                }
            }
            //加载失败
            is LoadState.Error -> Error(pagingData, refreshState)
            //加载中
            LoadState.Loading -> Loading(refreshState)
        }

        //如果在加载途中遇到错误的话，pagingData的状态为append
        when (pagingData.loadState.append) {
            //加载失败
            is LoadState.Error -> Error(pagingData, refreshState)
            //加载中
            is LoadState.Loading -> Loading(refreshState)
            //加载完成
            is LoadState.NotLoading -> {

            }
        }
    }

    /**
     * 未加载且未观察到错误
     */
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    private fun NotLoading(
        refreshState: SwipeRefreshState,
        viewModel: ViewModel,
        content: @Composable () -> Unit
    ) {

        content()

        //让刷新头停留一下子再收回去
        viewModel.sleepTime(millis = 1000) {
            refreshState.isRefreshing = false
        }
    }

    /**
     * 加载失败
     */
    @Composable
    private fun <T : Any> Error(
        pagingData: LazyPagingItems<T>,
        refreshState: SwipeRefreshState
    ) {
        refreshState.isRefreshing = false
        ErrorComposable {
            pagingData.refresh()
        }
    }

    /**
     * 加载中
     */
    @Composable
    private fun Loading(refreshState: SwipeRefreshState) {
        //显示刷新头
        if (!refreshState.isRefreshing) refreshState.isRefreshing = true
    }

}
