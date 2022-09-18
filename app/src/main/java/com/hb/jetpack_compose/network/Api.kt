package com.hb.jetpack_compose.network

import com.hb.jetpack_compose.ui.model.ArticleListResult
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("article/list/{page}/json")
    suspend fun getHomeArticleList(@Path("page") page:Int):ApiResult<ArticleListResult>

}