package com.example.creatorshub.screens.services

import com.example.creatorshub.data.BackendApiService
import com.example.creatorshub.models.ServiceResponse
import retrofit2.Call

class ServicesModel(private val client: BackendApiService) {

    fun fetchServices(): Call<List<ServiceResponse>> {
        return client.getServices()
    }
}
