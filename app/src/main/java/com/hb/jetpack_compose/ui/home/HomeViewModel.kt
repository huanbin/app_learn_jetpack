package com.hb.jetpack_compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hb.jetpack_compose.model.Banner
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private val _bannerStateFlow = MutableStateFlow<List<Banner>>(listOf())

    val bannerStateFlow = _bannerStateFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        listOf()
    )

    init {
        viewModelScope.launch {
            flow {
                Timber.tag("hb").d("bannerStateFlow1")
                val homeBanner = RetrofitApi.getInstance().getHomeBanner().data
                emit(homeBanner)
            }.catch {
                Timber.tag("hb").e(it)
            }.collectLatest {
                Timber.tag("hb").d("bannerStateFlow2")
                _bannerStateFlow.value = it
            }
        }
    }

    val pager = pagerFlow { page, pageSize ->
        Timber.tag("hb").d("pagerFlow")
        getHomeArticleList(page, pageSize).data.articleItemData
    }.flow.cachedIn(viewModelScope)

}