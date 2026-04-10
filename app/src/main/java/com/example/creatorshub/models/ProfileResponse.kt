package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Maps to a row in the Supabase "profiles" table.
 * Column names in Supabase are snake_case; @SerializedName handles the mapping.
 */
data class ProfileResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("first_name") val firstName: String = "",
    @SerializedName("last_name") val lastName: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("avatar_url") val avatar_url: String? = null
)
