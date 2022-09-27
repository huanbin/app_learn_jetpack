package com.hb.jetpack_compose.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.hb.jetpack_compose.R
import com.hb.jetpack_compose.ui.BaseFragment

class ArticleDetailFragment : BaseFragment() {

    private val viewModel by viewModels<ArticleDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return createComposeView { Screen(viewModel) { findNavController().navigateUp() } }
    }

    @Composable
    fun Screen(viewModel: ArticleDetailViewModel, onNavigate: (id: Int) -> Unit) {
        val urlState = viewModel.articleUrlState.collectAsState()
        val webViewState = rememberWebViewState(url = urlState.value)
        print("文章link = ${urlState.value}")
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                elevation = 0.dp,
                modifier = Modifier
                    .background(colorResource(id = R.color.primaryDarkColor))
                    .systemBarsPadding()
            ) {
                IconButton(onClick = { onNavigate?.invoke(0) }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White
                    )
                }
                Text(
                    text = "文章详情",
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.colorOnPrimary)
                )
            }

            WebView(state = webViewState, onCreated = {
                //必须添加，否则很多网页无法正常打开
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.settings.databaseEnabled = true
            })
        }
    }
}