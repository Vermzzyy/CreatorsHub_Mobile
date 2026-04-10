package com.example.creatorshub.screens.login.model

import com.google.gson.annotations.SerializedName

/**
 * Supabase Auth login response.
 * Supabase returns "access_token", not "token".
 */
data class LoginResponse(
    @SerializedName("access_token") val access_token: String? = null,
    @SerializedName("token_type") val token_type: String? = null,
    @SerializedName("refresh_token") val refresh_token: String? = null,
    @SerializedName("user") val user: UserInfo? = null
) {
    data class UserInfo(
        val id: String? = null,
        val email: String? = null
    )
}