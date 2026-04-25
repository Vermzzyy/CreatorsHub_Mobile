package com.example.creatorshub.shared

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://wilhjpvrfrscquauuvle.supabase.co/"
    private const val SUPABASE_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndpbGhqcHZyZnJzY3F1YXV1dmxlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU4Mjc0MjcsImV4cCI6MjA5MTQwMzQyN30.gEiI4MAIF-i1BG5C-ACV1Q7H_f3Az4ZD81kKbmoB6hU"

    /**
     * Auth client — used for login and register (no user Bearer token needed).
     * Points to Supabase Auth API (/auth/v1/).
     */
    fun createAuthClient(): AuthApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("apikey", SUPABASE_KEY)
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    /**
     * Data client — used for profile, update profile, change password.
     * Points to Supabase PostgREST API (/rest/v1/) and auth user endpoint.
     * Requires a valid Bearer token from session.
     */
    fun create(context: Context): ApiService {
        val sessionManager = SessionManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sessionManager.getToken()
                val requestBuilder = chain.request().newBuilder()
                    .header("apikey", SUPABASE_KEY)
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
            .create(ApiService::class.java)
    }
}
