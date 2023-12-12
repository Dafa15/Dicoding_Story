package com.example.dicodingstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.model.UserToken
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.StoryResponse

class MapsViewModel (private val storyRepository: StoryRepository, private val preference: UserPreference) : ViewModel() {

    fun getToken(): LiveData<UserToken> {
        return preference.userToken.asLiveData()
    }

    suspend fun getStoriesLocation(token: String): StoryResponse {
        return storyRepository.getLocation(token)
    }


}