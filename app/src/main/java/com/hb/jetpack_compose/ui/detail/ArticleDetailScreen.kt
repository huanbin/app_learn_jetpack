package com.hb.jetpack_compose.ui.detail


import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.hb.jetpack_compose.R

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DetailScreen(
    viewModel: ArticleDetailViewModel = viewModel(),
    onNavigate: (id: Int) -> Unit
) {
    val urlState = viewModel.articleUrlState.collectAsStateWithLifecycle()
    val webViewState = rememberWebViewState(url = urlState.value)

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
        Box(modifier = Modifier.fillMaxSize()) {

            WebView(state = webViewState, onCreated = {
                //必须添加，否则很多网页无法正常打开
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.settings.databaseEnabled = true
            }, client = remember {
                CustomWebViewClient()
            })

            CircularProgressIndicator(
                progress = if (webViewState.loadingState is LoadingState.Loading) (webViewState.loadingState as LoadingState.Loading).progress else 0f,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
    }
}

class CustomWebViewClient : AccompanistWebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?, request: WebResourceRequest?
    ): Boolean {
        println("shouldOverrideUrlLoading = ${request?.url.toString()}")

        if (request?.url.toString().startsWith("http") or request?.url.toString()
                .startsWith("https")
        ) {
            return super.shouldOverrideUrlLoading(view, request)
        }
        return true
    }
}
