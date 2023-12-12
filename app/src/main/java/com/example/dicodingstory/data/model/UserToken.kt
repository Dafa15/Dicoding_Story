package com.example.dicodingstory.data.model

import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("token")
    val token: String
)