package com.hb.jetpack_compose.ui.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hb.jetpack_compose.network.RetrofitApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val lazyListState = LazyListState()
    
    init {
        println("HomeViewModel")
        viewModelScope.launch {
            val articleListResult = RetrofitApi.getInstance().getHomeArticleList(0)
            println(articleListResult.data)
        }
    }
}