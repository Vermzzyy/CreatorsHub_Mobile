package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Maps the Spring Boot backend response for /api/v1/orders/my.
 */
data class OrderModel(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("serviceTitle") val serviceTitle: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("status") val status: String? = "PENDING",
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)
