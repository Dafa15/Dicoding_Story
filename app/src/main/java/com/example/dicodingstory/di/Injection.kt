package com.example.dicodingstory.di

import android.content.Context
import com.example.dicodingstory.api.ApiConfig
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.local.StoryDatabase
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        val storyDatabase = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(storyDatabase, apiService, pref)
    }
}