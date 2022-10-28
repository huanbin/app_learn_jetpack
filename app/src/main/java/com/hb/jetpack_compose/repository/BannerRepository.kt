package com.hb.jetpack_compose.repository

import com.hb.jetpack_compose.network.RetrofitApi
import kotlinx.coroutines.flow.flow

class BannerRepository {
    val bannerFlow = flow {
        val homeBanner = RetrofitApi.getInstance().getHomeBanner().data
        emit(homeBanner)
    }
}