package com.example.firebaseecom.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.firebaseecom.local.ProductDao
import com.example.firebaseecom.model.ProductHomeModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class ProductHomePagingSource (
    private val productDao: ProductDao
):PagingSource<Int,ProductHomeModel>() {
    override fun getRefreshKey(state: PagingState<Int, ProductHomeModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductHomeModel> {
        val page = params.key ?: 0

        return try {
            val entities =productDao.getProductByPage(params.loadSize, page * params.loadSize)
            Log.d("pageData",entities.toString())

            // simulate page loading
            if (page != 0) delay(1000)

            LoadResult.Page(
                data = entities,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}