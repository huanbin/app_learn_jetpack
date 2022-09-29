package com.hb.jetpack_compose.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hb.jetpack_compose.model.ArticleItemData
import com.hb.jetpack_compose.network.Api
import com.hb.jetpack_compose.network.RetrofitApi

abstract class ArticleDataSource : PagingSource<Int, ArticleItemData>() {

    companion object {
        val PageSize = 10
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleItemData>): Int? {

        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    abstract suspend fun Api.getData(page: Int, pageSize: Int): List<ArticleItemData>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleItemData> {
        val nextPage = params.key ?: 0
        try {
            //val homeArticleResult = RetrofitApi.getInstance().getHomeArticleList(nextPage, PageSize)
            //println("current page=${homeArticleResult.data.curPage}")
            //delay(3000)
            val dataList = RetrofitApi.getInstance().getData(nextPage, params.loadSize)
            return LoadResult.Page(
                //homeArticleResult.data.articleItemData,
                data = dataList,
                prevKey = if (nextPage == 0) null else nextPage.minus(1),
                //前端页码是从0开始的，后端页码从1开始的
                nextKey = if (dataList.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            println("ArticleDataSource Error=$e")
            return LoadResult.Error(e)
        }
    }

}

class A(var name: String)

fun test(m: A.() -> Unit) {
    val a = A("hello")
    a.m()
}

fun main() {
    test {
        name = "good"
    }
}