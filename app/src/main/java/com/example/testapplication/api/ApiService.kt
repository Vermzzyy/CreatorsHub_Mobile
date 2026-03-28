package com.example.testapplication.api

import com.example.testapplication.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiService {

    @POST("auth/v1/token?grant_type=password")
    fun login(@Body request: LoginRequest): retrofit2.Call<LoginResponse>

    @POST("auth/v1/signup")
    fun register(@Body request: RegisterRequest): retrofit2.Call<RegisterResponse>

    @Headers("Accept: application/vnd.pgrst.object+json")
    @GET("rest/v1/profiles?select=*")
    fun dashboard(@Header("Authorization") token: String): retrofit2.Call<DashboardResponse>

    @Headers("Accept: application/vnd.pgrst.object+json")
    @GET("rest/v1/profiles?select=*")
    fun profile(@Header("Authorization") token: String): retrofit2.Call<ProfileResponse>

    @Headers("Prefer: return=representation", "Accept: application/vnd.pgrst.object+json")
    @retrofit2.http.PATCH("rest/v1/profiles")
    fun updateProfile(
        @retrofit2.http.Query("id") idQuery: String,
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): retrofit2.Call<ProfileResponse>

    @retrofit2.http.PUT("auth/v1/user")
    fun changePassword(@Header("Authorization") token: String, @Body request: ChangePasswordRequest): retrofit2.Call<GenericResponse>

    @POST("storage/v1/object/avatars/{filename}")
    fun uploadAvatar(
        @retrofit2.http.Path("filename") filename: String,
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String,
        @Body file: okhttp3.RequestBody
    ): retrofit2.Call<GenericResponse>
}