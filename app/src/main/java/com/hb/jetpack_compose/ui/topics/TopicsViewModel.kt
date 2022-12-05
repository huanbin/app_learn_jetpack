package com.hb.jetpack_compose.ui.topics

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.*

class TopicsViewModel : ViewModel() {

    var lazyListStateList: MutableList<LazyListState> = arrayListOf()

    val projectCategoryList = flow {
        println("projectCategoryList")
        emit(listOf())
        val categoryList = RetrofitApi.getInstance().getProjectCategory().data

        repeat(categoryList.size) {
            lazyListStateList.add(LazyListState())
        }

        emit(categoryList)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    private val index = MutableStateFlow(0)

    val projectPagerFlow: Flow<PagingData<ArticleItemData>>
        get() = _loadArticle

    private val _loadArticle = pagerFlow { page, pageSize ->
        getProjectList(page, pageSize, projectCategoryList.value[index.value].id).data.datas
    }.flow.cachedIn(viewModelScope)


    fun updateIndex(value: Int) {
        index.value = value
    }
}