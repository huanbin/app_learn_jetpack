package com.hb.jetpack_compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hb.jetpack_compose.model.Banner
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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
            try {
                Timber.tag("hb").d("bannerStateFlow1")
                val homeBanner = RetrofitApi.getInstance().getHomeBanner().data
                _bannerStateFlow.value = homeBanner
            } catch (e: Exception) {
                Timber.tag("hb").e(e)
            }
        }
    }

    val pager = pagerFlow { page, pageSize ->
        Timber.tag("hb").d("pagerFlow")
        getHomeArticleList(page, pageSize).data.articleItemData
    }.flow.cachedIn(viewModelScope)

}