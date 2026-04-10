package com.example.creatorshub.screens.settings

import com.example.creatorshub.data.ApiService
import com.example.creatorshub.models.*
import okhttp3.RequestBody
import retrofit2.Call

class SettingsModel(private val api: ApiService) {

    fun getProfile(): Call<List<ProfileResponse>> = api.profile("*")

    fun updateProfile(userId: String, request: UpdateProfileRequest): Call<List<ProfileResponse>> =
        api.updateProfile("eq.$userId", request)

    fun changePassword(request: ChangePasswordRequest): Call<GenericResponse> =
        api.changePassword(request)

    fun uploadAvatar(filename: String, body: RequestBody): Call<GenericResponse> =
        api.uploadAvatar(filename, "image/jpeg", body)
}