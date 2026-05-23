package com.example.creatorshub.models

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST /api/v1/orders.
 */
data class CreateOrderRequest(
    @SerializedName("serviceId") val serviceId: Long,
    @SerializedName("instructions") val instructions: String
)
