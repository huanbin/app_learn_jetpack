package com.hb.jetpack_compose.ui.home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.model.Banner
import timber.log.Timber


@OptIn(
    ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), onNavigate: (urlArticle: String) -> Unit) {
    val lazyListState = rememberLazyListState()
    val pager = viewModel.pager.collectAsLazyPagingItems()
    val pagerState = rememberPagerState()
    val bannerDatas by viewModel.bannerStateFlow.collectAsStateWithLifecycle()

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .statusBarsPadding()
    ) {
        item {
            Banner(pagerState = pagerState, bannerDatas = bannerDatas, onNavigate = {
                onNavigate(it)
            })
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
        itemsIndexed(pager) { index, value ->
            ArticleItemLayout(value = pager[index], onClickItem = {
                onNavigate.invoke(it!!.link)
            })
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleItemLayout(value: ArticleItemData?, onClickItem: (value: ArticleItemData?) -> Unit) {
//    Column(modifier = Modifier.fillMaxWidth()) {
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
                    contentDescription = "??????",
                    modifier = Modifier
                )
            })
        }, secondaryText = null, singleLineSecondaryText = false, overlineText = null,
//                trailing = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            Icons.Outlined.MoreVert,
//                            tint = colorResource(id = R.color.primaryColor),
//                            contentDescription = "??????",
//                            modifier = Modifier.rotate(90f)
//                        )
//                    }
//                },
            text = {
                Text(text = "${value?.title}", maxLines = 3)
            })
    }
//    Divider(color = Color.LightGray, thickness = 0.5.dp)
//    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banner(
    pagerState: PagerState = rememberPagerState(),
    bannerDatas: List<Banner>,
    onNavigate: (String) -> Unit
) {
    Timber.tag("hb").d("Banner")
    HorizontalPager(count = bannerDatas.size,
        contentPadding = PaddingValues(top = 25.dp),
        state = pagerState,
        modifier = Modifier
            .clickable {
                onNavigate(bannerDatas[pagerState.currentPage].url)
            }
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
    ) { page ->
        AsyncImage(
            model = bannerDatas[page].imagePath,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}