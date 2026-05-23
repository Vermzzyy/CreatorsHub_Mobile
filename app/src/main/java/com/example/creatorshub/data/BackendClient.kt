package com.example.creatorshub.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

object BackendClient {

    /**
     * Base URL for the Spring Boot backend.
     *
     * - Android Emulator → http://10.0.2.2:8080/
     * - Physical device  → http://<your-PC-local-IP>:8080/
     *   (find it with: ipconfig → IPv4 Address)
     */
    const val BASE_URL = "https://creatorsbackend-6f3r.onrender.com/"

    /** Retrofit client for authenticated backend API calls (services, orders). */
    fun create(context: Context): BackendApiService {
        val sessionManager = SessionManager(context)

        val client = OkHttpClient.Builder()
            .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                // Use backend token if available, otherwise fallback to Supabase token
                val token = sessionManager.getBackendToken() ?: sessionManager.getToken()
                val requestBuilder = chain.request().newBuilder()
                    .header("Content-Type", "application/json")

                if (token != null) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }

    /** Retrofit client for the Spring Boot login endpoint (no auth header). */
    fun createAuthClient(context: Context): BackendAuthService {
        val client = OkHttpClient.Builder()
            .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendAuthService::class.java)
    }
}

/** Minimal interface for Spring Boot login — returns a JWT token. */
interface BackendAuthService {
    @POST("api/v1/auth/login")
    fun login(@Body credentials: Map<String, String>): Call<Map<String, Any>>

    @POST("api/v1/auth/register")
    fun register(@Body data: Map<String, String>): Call<Map<String, Any>>
}

