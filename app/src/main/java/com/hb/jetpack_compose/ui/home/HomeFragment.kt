package com.hb.jetpack_compose.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.ui.BaseFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    @Composable
    override fun createView() {
        //确保viewmodel的生命周期（保存lazyListState）
        val viewModel by activityViewModels<HomeViewModel>()
        val lazyListState = viewModel.lazyListState
        val pager = viewModel.pager
        HomeScreen(lazyListState, pager)
    }
}


@Composable
fun HomeScreen(lazyListState: LazyListState, pager: Flow<PagingData<ArticleItemData>>) {
    ArticleList(lazyListState, pager)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleList(lazyListState: LazyListState, pager: Flow<PagingData<ArticleItemData>>) {
    println("rememberLazyListState firstVisibleItemIndex=${lazyListState.firstVisibleItemIndex}")

    val lazyPagingItems = pager.collectAsLazyPagingItems()

    val rememberCoroutineScope = rememberCoroutineScope()

    LazyColumn(state = lazyListState) {

        if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
            item {
                Text(
                    text = "Waiting for items to load from the backend",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        itemsIndexed(lazyPagingItems) { index, value ->
            ListItem(
                modifier = Modifier.clickable { },
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
                    Text(text = "content ${value?.title}")
                }
            )
            Divider(color = Color.Black, thickness = 0.5.dp)
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        rememberCoroutineScope.launch {
            lazyListState.animateScrollToItem(
                lazyListState.firstVisibleItemIndex,
                lazyListState.firstVisibleItemScrollOffset
            )
        }
    }
}