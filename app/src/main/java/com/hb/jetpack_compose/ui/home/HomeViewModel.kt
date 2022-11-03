package com.hb.jetpack_compose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hb.jetpack_compose.network.RetrofitApi
import com.hb.jetpack_compose.repository.pagerFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class HomeViewModel : ViewModel() {

    /*private val _bannerStateFlow = MutableStateFlow<List<Banner>>(listOf())

    */
    /**
     * StateFlow是一种特殊的ShareFlow（特殊类型的Flow），专门用于向View公开UI状态
     * 1.总是有一个值
     * 2.有且只有一个值
     * 3.允许多个观察者（flow是共享的）
     * 4.总是重放订阅的最新值，并且与活跃的观察者个数无关
     *//*
//    一次性数据加载，这个用法和livedata一样
    val bannerStateFlow: StateFlow<List<Banner>> = _bannerStateFlow

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
    }*/

    val pager = pagerFlow { page, pageSize ->
        Timber.tag("hb").d("pagerFlow")
        getHomeArticleList(page, pageSize).data.articleItemData
    }.flow.cachedIn(viewModelScope)

    /**
     * 对于一次性事件，SharingStarted指定数据共享开始和结束，可以使用Lazily或者Eagerly
     * 对于其他的Flow，应该使用WhileSubscribed
     *
     */
    val bannerStateFlow = flow {
        Timber.tag("hb").d("bannerStateFlow")
        val homeBanner = RetrofitApi.getInstance().getHomeBanner().data
        emit(homeBanner)
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())
}