package com.hb.jetpack_compose.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleListResult(
    val curPage: Int,
    val datas: List<Data>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)