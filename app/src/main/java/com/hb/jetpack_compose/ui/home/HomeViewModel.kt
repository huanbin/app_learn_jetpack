package com.hb.jetpack_compose.ui.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hb.jetpack_compose.repository.ArticleDataSource

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val lazyListState = LazyListState()

    val pager = Pager(
        PagingConfig(
            pageSize = ArticleDataSource.PageSize,
//            prefetchDistance=10,
//            maxSize用于避免浪费更多内存资源，enablePlaceholders是配合maxSize一起，在用户滑动列表超过maxSize之后，用户反向滑动时暂时显示null item
            enablePlaceholders = true,
            maxSize = 200,
            initialLoadSize = 10
        )
    ) { ArticleDataSource() }.flow
        //.flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

}