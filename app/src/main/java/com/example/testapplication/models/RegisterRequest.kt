package com.example.testapplication.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val data: UserData
)

data class UserData(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String
)
