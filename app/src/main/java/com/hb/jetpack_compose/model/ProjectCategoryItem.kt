package com.hb.jetpack_compose.model

@kotlinx.serialization.Serializable
data class ProjectCategoryItem(
//    val articleList: List<ArticleItemData>,
    val author: String,
//    val children: List<Any>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val type: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)