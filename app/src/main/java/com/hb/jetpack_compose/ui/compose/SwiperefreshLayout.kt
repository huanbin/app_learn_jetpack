package com.hb.jetpack_compose.ui.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.model.ArticleItemData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwiperefreshLayout(
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<ArticleItemData>,
) {

    val rememberSwipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading)

    SwipeRefresh(state = rememberSwipeRefreshState, onRefresh = {
        lazyPagingItems.refresh()
    }, indicator = { state, trigger ->
        GlowIndicator(
            swipeRefreshState = state, refreshTriggerDistance = trigger
        )
    }) {

        LazyColumn(state = lazyListState) {
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
                    Text(
                        text = "refresh occur error ${error.error.cause}",
                        modifier = Modifier
                            .clickable {
                                lazyPagingItems.retry()
                            }
                            .fillParentMaxSize()
                            .wrapContentSize()
                    )
                }
            }


            itemsIndexed(lazyPagingItems) { index, value ->
                ListItem(modifier = Modifier.clickable { },
                    icon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Outlined.FavoriteBorder,
                                tint = colorResource(id = R.color.primaryColor),
                                contentDescription = "收藏",
                                modifier = Modifier
                            )
                        }
                    },
                    secondaryText = null,
                    singleLineSecondaryText = false,
                    overlineText = null,
                    trailing = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                tint = colorResource(id = R.color.primaryColor),
                                contentDescription = "更多",
                                modifier = Modifier.rotate(90f)
                            )
                        }
                    },
                    text = {
                        Text(text = "${value?.title}")
                    })
                Divider(color = Color.Black, thickness = 0.5.dp)
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
        }

        if (lazyPagingItems.loadState.source.refresh is LoadState.NotLoading) {
            val refresh = lazyPagingItems.loadState.source.refresh
            if (refresh.endOfPaginationReached) {
                println("到达最后，没有更多数据")
            } else {
                println("没有到达最后，可以加载下一页数据")
            }
        }

        val rememberCoroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            rememberCoroutineScope.launch {
                lazyListState.animateScrollToItem(
                    lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset
                )
            }
        }
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