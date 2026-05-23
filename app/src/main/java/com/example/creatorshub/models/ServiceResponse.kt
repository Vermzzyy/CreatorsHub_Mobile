package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Maps the Spring Boot backend response for /api/v1/services.
 * Field names match the ServiceResponse DTO from the backend.
 */
data class ServiceResponse(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("title") val title: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("tags") val tags: List<String>? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null
)
