package com.example.testapplication.models

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val avatar_url: String? = null
)
