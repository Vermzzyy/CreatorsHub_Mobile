package com.example.testapplication.models

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
