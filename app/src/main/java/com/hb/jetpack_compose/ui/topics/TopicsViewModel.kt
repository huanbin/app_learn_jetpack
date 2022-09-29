package com.hb.jetpack_compose.ui.topics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hb.jetpack_compose.network.RetrofitApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class TopicsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    val pages = flow {
        val categoryList = RetrofitApi.getInstance().getProjectCategory().data
        emit(categoryList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
}