package com.example.dicodingstory.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dicodingstory.api.ApiService
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource (private val apiService: ApiService, private val pref: UserPreference) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.userToken.first().token
            val responseData = apiService.getStoriesPage("Bearer $token" ,position, params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}