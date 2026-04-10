package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Request body for Supabase Auth password update: PUT /auth/v1/user
 * Supabase uses "password" field only (does not validate old password natively).
 */
data class ChangePasswordRequest(
    @SerializedName("password") val password: String
)
