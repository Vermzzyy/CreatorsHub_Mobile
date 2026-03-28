package com.example.testapplication.models

data class ProfileResponse(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatar_url: String? = null
)
