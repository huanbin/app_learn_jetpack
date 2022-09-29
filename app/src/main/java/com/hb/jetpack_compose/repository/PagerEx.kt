package com.hb.jetpack_compose.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.network.Api

fun pagerFlow(loadData: suspend Api.(page: Int, pageSize: Int) -> List<ArticleItemData>) =
    Pager(
        PagingConfig(
            pageSize = ArticleDataSource.PageSize,
//            prefetchDistance=10,
//            maxSize用于避免浪费更多内存资源，enablePlaceholders是配合maxSize一起，在用户滑动列表超过maxSize之后，用户反向滑动时暂时显示null item
            enablePlaceholders = true, maxSize = 200, initialLoadSize = 10
        )
    ) {
        object : ArticleDataSource() {
            override suspend fun Api.getData(page: Int, pageSize: Int): List<ArticleItemData> {
                return loadData(page, pageSize)
                // return getHomeArticleList(page, pageSize).data.articleItemData
            }
        }
    }.flow
