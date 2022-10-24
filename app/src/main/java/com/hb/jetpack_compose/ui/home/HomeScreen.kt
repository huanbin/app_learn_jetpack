package com.hb.jetpack_compose.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hb.jetpack_compose.BottomNavigationViewHeight
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.ui.compose.SwiperefreshLayout


@OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), onNavigate: (urlArticle: String) -> Unit) {
    val lazyListState = rememberLazyListState()
    val pager = viewModel.pager.collectAsLazyPagingItems()
    val asPaddingValues = WindowInsets.systemBars.asPaddingValues()
    val bannerState = viewModel.bannerStateFlow.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState()

    SwiperefreshLayout(contentPadding = PaddingValues(
        //top = asPaddingValues.calculateTopPadding(),
        bottom = asPaddingValues.calculateBottomPadding() + BottomNavigationViewHeight.dp
    ), lazyListState = lazyListState, lazyPagingItems = pager, itemLayout = { index, value ->
        ArticleItemLayout(value = value) {
            onNavigate.invoke(value!!.link)
        }
    }) {
        HorizontalPager(count = bannerState.value.size,
//            contentPadding = PaddingValues(
//                top = asPaddingValues.calculateTopPadding()
//            ),
            state = pagerState, modifier = Modifier
                .clickable {
                    onNavigate?.invoke(bannerState.value[pagerState.currentPage].url)
                }
                .fillMaxWidth()
                .fillMaxHeight(0.25f)) { page ->
            AsyncImage(
                model = bannerState.value[page].imagePath,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Divider(color = Color.LightGray, thickness = 4.dp)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleItemLayout(value: ArticleItemData?, onClickItem: (value: ArticleItemData?) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (null == value) {
            ListItem {
                Text(
                    text = "Loading...", modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                )
            }
        } else {
            ListItem(modifier = Modifier
                .clickable { onClickItem.invoke(value) }
                .padding(vertical = 16.dp), icon = {
                IconToggleButton(checked = false, onCheckedChange = {

                }, content = {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        tint = colorResource(id = R.color.primaryColor),
                        contentDescription = "收藏",
                        modifier = Modifier
                    )
                })
            }, secondaryText = null, singleLineSecondaryText = false, overlineText = null,
//                trailing = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            Icons.Outlined.MoreVert,
//                            tint = colorResource(id = R.color.primaryColor),
//                            contentDescription = "更多",
//                            modifier = Modifier.rotate(90f)
//                        )
//                    }
//                },
                text = {
                    Text(text = "${value?.title}", maxLines = 3)
                })
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}