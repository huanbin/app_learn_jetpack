package com.hb.jetpack_compose.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.hb.jetpack_compose.BottomNavigationViewHeight
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.ui.BaseFragment
import com.hb.jetpack_compose.ui.compose.SwiperefreshLayout

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //确保viewmodel的生命周期（保存lazyListState）
        val viewModel by activityViewModels<HomeViewModel>()
        return createComposeView { HomeScreen(viewModel) { findNavController().navigate(R.id.list_item) } }
    }
}


@Composable
fun HomeScreen(viewModel: HomeViewModel, onNavigate: (Int) -> Unit) {
    val lazyListState = viewModel.lazyListState
    val pager = viewModel.pager.collectAsLazyPagingItems()
    val asPaddingValues = WindowInsets.systemBars.asPaddingValues()
    SwiperefreshLayout(
        contentPadding = PaddingValues(
            top = asPaddingValues.calculateTopPadding(),
            bottom = asPaddingValues.calculateBottomPadding() + BottomNavigationViewHeight.dp
        ),
        lazyListState,
        pager
    ) { index, value ->
        ArticleItemLayout(value = value) {
            onNavigate.invoke(R.id.list_item)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleItemLayout(value: ArticleItemData?, onClickItem: (value: ArticleItemData?) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListItem(modifier = Modifier.clickable { onClickItem.invoke(value) },
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
}