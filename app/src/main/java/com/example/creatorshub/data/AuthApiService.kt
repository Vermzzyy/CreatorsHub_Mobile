package com.example.creatorshub.data

import com.example.creatorshub.screens.login.model.LoginRequest
import com.example.creatorshub.screens.login.model.LoginResponse
import com.example.creatorshub.screens.register.model.RegisterRequest
import com.example.creatorshub.screens.register.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Supabase Auth API — endpoints for login and register.
 * Base URL: https://fmkdrxpdiroqsfblyavt.supabase.co/
 */
interface AuthApiService {

    // Login: POST /auth/v1/token?grant_type=password
    @POST("auth/v1/token?grant_type=password")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Register: POST /auth/v1/signup
    @POST("auth/v1/signup")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}
