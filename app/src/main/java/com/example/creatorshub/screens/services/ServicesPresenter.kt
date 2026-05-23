package com.example.creatorshub.screens.services

import com.example.creatorshub.models.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServicesPresenter(
    private val view: ServicesContract.View,
    private val model: ServicesModel
) : ServicesContract.Presenter {

    override fun loadServices() {
        view.showLoading()

        model.fetchServices().enqueue(object : Callback<List<ServiceResponse>> {

            override fun onResponse(
                call: Call<List<ServiceResponse>>,
                response: Response<List<ServiceResponse>>
            ) {
                view.hideLoading()

                if (response.isSuccessful) {
                    val services = response.body() ?: emptyList()
                    if (services.isEmpty()) {
                        view.showEmpty("No services available yet.")
                    } else {
                        view.showServices(services)
                    }
                } else {
                    view.showEmpty(
                        "Could not load services (${response.code()}).\nIs the backend server running?"
                    )
                }
            }

            override fun onFailure(call: Call<List<ServiceResponse>>, t: Throwable) {
                view.hideLoading()
                view.showEmpty("Cannot reach server. Make sure the backend is running.")
            }
        })
    }
}
