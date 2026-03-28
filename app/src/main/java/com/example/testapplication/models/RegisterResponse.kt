package com.example.testapplication.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("access_token") val token: String?,
    val id: String? = null
)
