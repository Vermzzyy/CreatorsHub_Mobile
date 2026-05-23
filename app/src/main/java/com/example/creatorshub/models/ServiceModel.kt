package com.example.creatorshub.models

/**
 * Lightweight model for the Services RecyclerView grid.
 * Populated from the backend ServiceResponse.
 */
data class ServiceModel(
    val id: Long,
    val name: String,
    val category: String,
    val price: String,
    val thumbnailUrl: String?
)