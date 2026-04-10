package com.example.creatorshub.screens.register

import com.example.creatorshub.data.AuthApiService
import com.example.creatorshub.screens.register.model.RegisterRequest
import com.example.creatorshub.screens.register.model.RegisterResponse
import retrofit2.Call

class RegisterModel(private val api: AuthApiService) {

    fun register(request: RegisterRequest): Call<RegisterResponse> {
        return api.register(request)
    }
}