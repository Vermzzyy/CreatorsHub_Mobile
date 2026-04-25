package com.example.creatorshub.screens.home

import com.example.creatorshub.shared.ApiService
import com.example.creatorshub.models.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Updated HomeModel to support profile fetching.
 */
class HomeModel(private val api: ApiService) {

    fun fetchProfile(callback: (String?) -> Unit) {
        api.profile("*").enqueue(object : Callback<List<ProfileResponse>> {
            override fun onResponse(
                call: Call<List<ProfileResponse>>,
                response: Response<List<ProfileResponse>>
            ) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    callback(response.body()!![0].firstName)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                callback(null)
            }
        })
    }
}