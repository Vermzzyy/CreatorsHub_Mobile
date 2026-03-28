package com.example.testapplication.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token") val token: String,
    val message: String? = null
)
