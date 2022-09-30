package com.hb.jetpack_compose.ui.topics

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.hb.jetpack_compose.ui.BaseFragment
import com.hb.jetpack_compose.ui.compose.SwiperefreshLayout
import com.hb.jetpack_compose.ui.home.ArticleItemLayout

class TopicsFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val topicsViewModel by activityViewModels<TopicsViewModel>()
        return createComposeView {
            Screen(viewModel = topicsViewModel) {
                findNavController().navigate(
                    //这里有一个bug
                    // 传递uri中必须有http，否则ViewModel中无法获取参数值。但是导航中定义deepLink时uri可以设置为hb.com/articles?url={url}
                    NavDeepLinkRequest.Builder.fromUri(Uri.parse("http://hb.com/articles?url=${it}"))
                        .setAction("android.intent.action.ACTION_READ_ARTICLE")
                        .setMimeType("type/subtype").build()
                )
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
    @Composable
    fun Screen(viewModel: TopicsViewModel, onNavgate: (url: String) -> Unit) {
        val pagerState = rememberPagerState()
        val projectCategoryList by viewModel.projectCategoryList.collectAsStateWithLifecycle()
        val index by viewModel.index.collectAsStateWithLifecycle()
        val lazyPagingItems = viewModel.projectPagerFlow.collectAsLazyPagingItems()
        var selectedIndex by remember {
            mutableStateOf(pagerState.currentPage)
        }

        LaunchedEffect(selectedIndex) {
            //pagerState.animateScrollToPage(selectedIndex)
            pagerState.scrollToPage(selectedIndex)
        }

        //监听pagerState状态，当用户滑动页面，更新selectedIndex，避免用户滑动之后无法再次选中上次的tab
        //LaunchedEffect跟随组合的声明周期运行，每次重组都会执行
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect {
                //只要pagerState.currentPage发生改变，就收集状态
                selectedIndex = it
                viewModel.updateIndex(it)
            }
        }

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
                            selectedIndex = index
                        },
                    )
                }
            }

            HorizontalPager(
                count = projectCategoryList.size,
                state = pagerState,
            ) { page ->
                SwiperefreshLayout(lazyListState = rememberLazyListState(),
                    lazyPagingItems = lazyPagingItems,
                    itemLayout = { _, data ->
                        ArticleItemLayout(value = data, onClickItem = {
                            onNavgate.invoke(it!!.link)
                        })
                    })
            }
        }
    }
}