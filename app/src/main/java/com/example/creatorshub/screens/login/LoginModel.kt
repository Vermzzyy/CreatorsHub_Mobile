package com.example.creatorshub.screens.login

import com.example.creatorshub.shared.AuthApiService
import com.example.creatorshub.screens.login.model.LoginRequest
import com.example.creatorshub.screens.login.model.LoginResponse
import retrofit2.Call

class LoginModel(private val api: AuthApiService) {

    fun login(request: LoginRequest): Call<LoginResponse> {
        return api.login(request)
    }
}