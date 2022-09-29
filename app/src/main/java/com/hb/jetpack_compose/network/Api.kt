package com.hb.jetpack_compose.network

import com.hb.jetpack_compose.model.ArticleListResult
import com.hb.jetpack_compose.model.Banner
import com.hb.jetpack_compose.model.ProjectCategoryItem
import com.hb.jetpack_compose.model.ProjectListResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("article/list/{page}/json")
    suspend fun getHomeArticleList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): ApiResult<ArticleListResult>

    //首页Banner
    @GET("banner/json")
    suspend fun getHomeBanner(): ApiResult<List<Banner>>

    //项目分类
    @GET("project/tree/json")
    suspend fun getProjectCategory(): ApiResult<List<ProjectCategoryItem>>

    //项目分类列表
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResult<ProjectListResult>
}