package com.hb.jetpack_compose.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ArticleDetailViewModel(stateHandle: SavedStateHandle) : ViewModel() {

    val articleUrlState = stateHandle.getStateFlow("url", "")

}