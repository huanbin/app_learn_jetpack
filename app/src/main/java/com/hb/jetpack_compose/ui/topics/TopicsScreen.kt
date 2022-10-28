package com.hb.jetpack_compose.ui.topics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.hb.jetpack_compose.ui.component.SwiperefreshLayout
import com.hb.jetpack_compose.ui.home.ArticleItemLayout
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun TopicScreen(
    viewModel: TopicsViewModel = viewModel(), onNavgate: (url: String) -> Unit
) {
    val pagerState = rememberPagerState()
    val projectCategoryList by viewModel.projectCategoryList.collectAsStateWithLifecycle()
    val lazyPagingItems = viewModel.projectPagerFlow.collectAsLazyPagingItems()
    val pagerLazyListState = projectCategoryList.associate {
        key(it.id) {
            it.id to rememberLazyListState()
        }
    }

    //监听pagerState状态，当用户滑动页面，更新selectedIndex，避免用户滑动之后无法再次选中上次的tab
    //LaunchedEffect跟随组合的声明周期运行，每次重组都会执行
    LaunchedEffect(pagerState.currentPage) {
        viewModel.updateIndex(pagerState.currentPage)
    }

    val coroutineScope = rememberCoroutineScope()

    Column {
        ScrollableTabRow(modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            ),
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }) {
            // Add tabs for all of our pages
            projectCategoryList.forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Text(text = tab.name, fontSize = 20.sp)
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        //viewModel.updateIndex(index)
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            count = projectCategoryList.size,
            state = pagerState,
        ) { page ->
            SwiperefreshLayout(lazyListState = pagerLazyListState.getValue(
                projectCategoryList[pagerState.currentPage].id
            ), lazyPagingItems = lazyPagingItems, itemLayout = { _, data ->
                ArticleItemLayout(value = data, onClickItem = {
                    onNavgate.invoke(it!!.link)
                })
            })
        }
    }
}
