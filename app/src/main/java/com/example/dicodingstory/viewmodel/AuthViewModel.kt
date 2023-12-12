package com.example.dicodingstory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingstory.data.StoryRepository
import com.example.dicodingstory.data.pref.UserPreference
import com.example.dicodingstory.data.response.LoginResponse
import com.example.dicodingstory.data.response.RegisterResponse

class AuthViewModel(private val storyRepository: StoryRepository, private val preference: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        startLoading()
        return storyRepository.register(name, email, password)
    }

    suspend fun saveToken(token: String) {
         preference.saveToken(token)
    }
    suspend fun clearToken() {
        preference.clearToken()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        startLoading()
        val response = storyRepository.login(email, password)
        stopLoading()
        return response
    }

    private fun startLoading() {
        _isLoading.postValue(true)
    }

    private fun stopLoading() {
        _isLoading.postValue(false)
    }
}