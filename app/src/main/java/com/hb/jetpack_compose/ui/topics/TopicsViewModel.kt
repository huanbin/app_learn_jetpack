package com.hb.jetpack_compose.ui.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class TopicsViewModel : ViewModel() {

    val projectCategoryList = flow {
        println("projectCategoryList")
        val categoryList = RetrofitApi.getInstance().getProjectCategory().data
        emit(categoryList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    private val index = MutableStateFlow(0)

    val projectPagerFlow
        get() = pagerFlow { page, pageSize ->
            getProjectList(page, pageSize, projectCategoryList.value[index.value].id).data.datas
        }.flow.cachedIn(viewModelScope)


    fun updateIndex(value: Int) {
        index.value = value
    }
}