package com.example.dicodingstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.model.UserToken
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddViewModel (private val storyRepository: StoryRepository, private val preference: UserPreference) : ViewModel() {

     suspend fun uploadStory (token: String, file: MultipartBody.Part, description: RequestBody): AddStoryResponse {
        return storyRepository.addStories("Bearer $token", file, description)
    }

    fun getToken(): LiveData<UserToken> {
        return preference.userToken.asLiveData()
    }
}