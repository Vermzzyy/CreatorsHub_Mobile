package com.example.creatorshub.screens.services

import com.example.creatorshub.data.BackendApiService
import com.example.creatorshub.models.CreateOrderRequest
import com.example.creatorshub.models.GenericResponse
import retrofit2.Call

class ServiceDetailModel(private val client: BackendApiService) {

    fun createOrder(serviceId: Long, instructions: String): Call<GenericResponse> {
        return client.createOrder(CreateOrderRequest(serviceId, instructions))
    }
}
