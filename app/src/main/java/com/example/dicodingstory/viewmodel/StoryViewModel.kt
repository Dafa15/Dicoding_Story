package com.example.dicodingstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.model.UserToken
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.ListStoryItem

class StoryViewModel (storyRepository: StoryRepository, private val preference: UserPreference) : ViewModel() {

    fun getToken(): LiveData<UserToken> {
        return preference.userToken.asLiveData()
    }

    val story: LiveData<PagingData<ListStoryItem>> by lazy {
        storyRepository.getStoriesPaging().cachedIn(viewModelScope)
    }

}