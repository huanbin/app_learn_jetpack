package com.hb.jetpack_compose.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hb.jetpack_compose.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> SwiperefreshLayout(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<T>,
    itemLayout: @Composable (index: Int, value: T?) -> Unit,
    itemHeaderLayout: (@Composable () -> Unit)? = null
) {

    val rememberSwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading)

    SwipeRefresh(
        state = rememberSwipeRefreshState,
        onRefresh = {
            lazyPagingItems.refresh()
        },
        indicator = { state, trigger ->
            GlowIndicator(
                swipeRefreshState = state, refreshTriggerDistance = trigger
            )
        }) {

        LazyColumn(state = lazyListState, contentPadding = contentPadding) {
            if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .wrapContentSize(Alignment.Center),
                        color = colorResource(id = R.color.primaryColor),
                        strokeWidth = 2.dp
                    )
                }
            }

            if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                val error = lazyPagingItems.loadState.refresh as LoadState.Error
                item {
                    Text(text = "refresh occur error ${error.error.cause}",
                        modifier = Modifier
                            .clickable {
                                lazyPagingItems.retry()
                            }
                            .fillParentMaxSize()
                            .wrapContentSize())
                }
            }

            item {
                itemHeaderLayout?.invoke()
            }
            itemsIndexed(lazyPagingItems) { index, value ->
                itemLayout(index, value)
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        color = colorResource(id = R.color.primaryColor),
                        strokeWidth = 2.dp
                    )
                }
            }
//            空页面
            if (lazyPagingItems.loadState.source.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0) {
                item {
                    ListItem {
                        Text(text = "暂时没有数据")
                    }
                }
            }
        }

//        val rememberCoroutineScope = rememberCoroutineScope()
//        LaunchedEffect(Unit) {
//            rememberCoroutineScope.launch {
//                lazyListState.animateScrollToItem(
//                    lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset
//                )
//            }
//        }
    }
}

/**
 * A custom indicator which displays a glow and progress indicator
 */
@Composable
fun GlowIndicator(
    swipeRefreshState: SwipeRefreshState,
    refreshTriggerDistance: Dp,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        Modifier
            .drawWithCache {
                onDrawBehind {
                    val distance = refreshTriggerDistance.toPx()
                    val progress = (swipeRefreshState.indicatorOffset / distance).coerceIn(0f, 1f)
                    // We draw a translucent glow
                    val brush = Brush.verticalGradient(
                        0f to color.copy(alpha = 0.45f), 1f to color.copy(alpha = 0f)
                    )
                    // And fade the glow in/out based on the swipe progress
                    drawRect(brush = brush, alpha = FastOutSlowInEasing.transform(progress))
                }
            }
            .fillMaxWidth()
            .height(72.dp)) {
        if (swipeRefreshState.isRefreshing) {
            // If we're refreshing, show an indeterminate progress indicator
            LinearProgressIndicator(Modifier.fillMaxWidth())
        } else {
            // Otherwise we display a determinate progress indicator with the current swipe progress
            val trigger = with(LocalDensity.current) { refreshTriggerDistance.toPx() }
            val progress = (swipeRefreshState.indicatorOffset / trigger).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun <T : Any> SwipeRefreshContent(
    viewModel: ViewModel,
    lazyPagingListData: LazyPagingItems<T>,
    state: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit = {},
    content: @Composable (index: Int, data: T) -> Unit,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    swipeEnabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxSize()) {
        //状态需要提升，实现刷新效果
        val refreshState = rememberSwipeRefreshState(false)
        val endList = remember {
            mutableStateOf(false)
        }
        SwipeRefresh(
            state = refreshState,
            swipeEnabled = swipeEnabled,
            onRefresh = {
                //显示刷新头
                refreshState.isRefreshing = true
                //刷新数据
                lazyPagingListData.refresh()
                viewModel.sleepTime(millis = 1000) {
                    refreshState.isRefreshing = false
                }
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    // Pass the SwipeRefreshState + trigger through
                    state = state,
                    refreshTriggerDistance = trigger,
                    // Enable the scale animation
                    scale = true,
                    // Change the color and shape
                    backgroundColor = Color.White,
                    contentColor = Color.Red,
                    shape = RoundedCornerShape(100),
                    largeIndication = false,
                )
            }
        ) {
            //列表数据
            PagingStateUtil().pagingStateUtil(lazyPagingListData, refreshState, viewModel) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    contentPadding = contentPaddingValues
                ) {
                    itemContent()
                    itemsIndexed(lazyPagingListData) { index, data ->
                        content(index, data!!)
                    }
                    //加载更多，没有更多数据状态页
                    if (lazyPagingListData.loadState.append is LoadState.NotLoading && lazyPagingListData.loadState.append.endOfPaginationReached) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize()
                            ) {
                                Text(
                                    text = "没有更多数据",
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}