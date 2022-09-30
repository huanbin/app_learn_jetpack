package com.hb.jetpack_compose.ui.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.*

class TopicsViewModel : ViewModel() {

    val projectCategoryList = flow {
        val categoryList = RetrofitApi.getInstance().getProjectCategory().data
        emit(categoryList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

    val index = MutableStateFlow(0)

    val projectPagerFlow = projectCategoryList.filter { it.isNotEmpty() }.combine(index) { a, b ->
        a[b]
    }.flatMapLatest {
        pagerFlow { page, pageSize ->
            getProjectList(page, pageSize, it.id).data.datas
        }
    }.cachedIn(viewModelScope)

//    val projectPagerFlow = pagerFlow { page, pageSize ->
//        getProjectList(page, pageSize, it.id).data.datas
//    }.cachedIn(viewModelScope)

    fun updateIndex(it: Int) {
        index.value = it
    }
}