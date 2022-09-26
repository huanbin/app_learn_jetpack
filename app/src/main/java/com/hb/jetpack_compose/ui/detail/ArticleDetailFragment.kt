package com.hb.jetpack_compose.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.fragment.app.viewModels
import com.hb.jetpack_compose.ui.BaseFragment

class ArticleDetailFragment : BaseFragment() {

    companion object {
        fun newInstance() = ArticleDetailFragment()
    }

    private var viewModel = viewModels<ArticleDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return createComposeView {
            Column {
                TopAppBar {
                    Text(text = "文章详情")
                }
            }
        }
    }
}