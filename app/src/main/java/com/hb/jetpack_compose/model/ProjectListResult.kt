package com.hb.jetpack_compose.model

@kotlinx.serialization.Serializable
data class ProjectListResult(
    val curPage: Int,
    val datas: List<ArticleItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)