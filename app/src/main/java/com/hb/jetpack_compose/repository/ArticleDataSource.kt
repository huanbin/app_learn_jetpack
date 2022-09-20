package com.hb.jetpack_compose.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.network.RetrofitApi

class ArticleDataSource() : PagingSource<Int, ArticleItemData>() {

    val DataBatchSize = 10


    override fun getRefreshKey(state: PagingState<Int, ArticleItemData>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleItemData> {
        val nextPage = params.key ?: 0
        val homeArticleResult = RetrofitApi.getInstance().getHomeArticleList(nextPage)
        try {
            return LoadResult.Page(
                homeArticleResult.data.articleItemData,
                prevKey = if (nextPage == 0) null else nextPage.minus(1),
                nextKey = homeArticleResult.data.curPage.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}