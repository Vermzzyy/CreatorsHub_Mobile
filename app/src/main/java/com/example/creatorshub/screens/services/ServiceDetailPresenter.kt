package com.example.creatorshub.screens.services

import com.example.creatorshub.models.GenericResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceDetailPresenter(
    private val view: ServiceDetailContract.View,
    private val model: ServiceDetailModel
) : ServiceDetailContract.Presenter {

    override fun bookService(serviceId: Long, instructions: String) {
        if (serviceId == -1L) {
            view.showBookingError("Invalid service.")
            return
        }

        view.disableBookButton()
        view.showBookingLoading()

        model.createOrder(serviceId, instructions)
            .enqueue(object : Callback<GenericResponse> {

                override fun onResponse(
                    call: Call<GenericResponse>,
                    response: Response<GenericResponse>
                ) {
                    view.enableBookButton()

                    when {
                        response.isSuccessful -> view.showBookingSuccess()
                        response.code() == 401 || response.code() == 403 ->
                            view.showBookingError("Please log in to book a service.")
                        else ->
                            view.showBookingError("Failed to book (${response.code()}). Try again.")
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    view.enableBookButton()
                    view.showBookingError("No connection. Make sure the backend is running.")
                }
            })
    }
}
