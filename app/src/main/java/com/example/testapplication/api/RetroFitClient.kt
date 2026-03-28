package com.example.testapplication.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://fmkdrxpdiroqsfblyavt.supabase.co/"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZta2RyeHBkaXJvcXNmYmx5YXZ0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI4MTgxMjMsImV4cCI6MjA4ODM5NDEyM30.HOh_AWM7q_o8CGToiaBvZN2cYFBRRcHc4VuUTggjrhE"

    val instance: ApiService by lazy {

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("apikey", SUPABASE_KEY)
            
            if (original.header("Authorization") == null) {
                requestBuilder.header("Authorization", "Bearer $SUPABASE_KEY")
            }
                
            chain.proceed(requestBuilder.build())
        }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}