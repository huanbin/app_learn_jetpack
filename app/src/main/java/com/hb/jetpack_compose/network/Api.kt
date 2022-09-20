package com.hb.jetpack_compose.network

import com.hb.jetpack_compose.model.ArticleListResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("article/list/{page}/json")
    suspend fun getHomeArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): ApiResult<ArticleListResult>

}