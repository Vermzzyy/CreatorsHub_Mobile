package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Request body for updating the profiles table in Supabase.
 * Field names must match the column names in the Supabase table.
 */
data class UpdateProfileRequest(
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("avatar_url") val avatar_url: String? = null
)
