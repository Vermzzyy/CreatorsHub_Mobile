package com.example.creatorshub.shared

import com.example.creatorshub.models.GenericResponse
import com.example.creatorshub.models.ProfileResponse
import com.example.creatorshub.models.UpdateProfileRequest
import com.example.creatorshub.models.ChangePasswordRequest
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Supabase PostgREST & Auth user API — for protected endpoints.
 * Requires Bearer token (access_token from login).
 * Base URL: https://fmkdrxpdiroqsfblyavt.supabase.co/
 */
interface ApiService {

    // 👤 GET PROFILE — reads from "profiles" table via PostgREST
    // Returns a list; we take the first item (filtered by RLS using Bearer token)
    @GET("rest/v1/profiles")
    @Headers("Prefer: return=representation")
    fun profile(@Query("select") select: String): Call<List<ProfileResponse>>

    // ✏️ UPDATE PROFILE — patches the row where id matches current user
    @PATCH("rest/v1/profiles")
    @Headers("Prefer: return=representation")
    fun updateProfile(
        @Query("id") filter: String,
        @Body request: UpdateProfileRequest
    ): Call<List<ProfileResponse>>

    // 🔒 CHANGE PASSWORD — uses Supabase Auth user update endpoint
    @PUT("auth/v1/user")
    fun changePassword(@Body request: ChangePasswordRequest): Call<GenericResponse>

    // 📸 UPLOAD AVATAR — uploads directly to Supabase Storage
    @PUT("storage/v1/object/avatars/{filename}")
    fun uploadAvatar(
        @Path("filename") filename: String,
        @Header("Content-Type") contentType: String,
        @Body body: RequestBody
    ): Call<GenericResponse>
}