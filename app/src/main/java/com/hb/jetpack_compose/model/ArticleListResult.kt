package com.hb.jetpack_compose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleListResult(
    val curPage: Int,
    @SerialName("datas")
    val articleItemData: List<ArticleItemData>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)