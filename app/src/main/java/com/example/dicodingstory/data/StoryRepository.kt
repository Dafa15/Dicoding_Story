package com.example.dicodingstory.data


import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dicodingstory.api.ApiService
import com.example.dicodingstory.data.local.StoryDatabase
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.AddStoryResponse
import com.example.dicodingstory.data.response.ListStoryItem
import com.example.dicodingstory.data.response.RegisterResponse
import com.example.dicodingstory.data.response.LoginResponse
import com.example.dicodingstory.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val pref: UserPreference
) {

     suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun addStories(token: String, file: MultipartBody.Part, description: RequestBody): AddStoryResponse {
        return apiService.addStory(token, file, description)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getLocation(token: String): StoryResponse {
        return apiService.getStoriesWithLocation("Bearer $token")
    }

    fun getStoriesPaging(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = RemoteStoryMediator(storyDatabase, apiService, pref),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            pref: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase,apiService, pref)
            }.also { instance = it }
    }
}