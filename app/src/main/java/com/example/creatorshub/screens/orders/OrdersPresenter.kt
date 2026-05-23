package com.example.creatorshub.screens.orders

import com.example.creatorshub.models.OrderModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersPresenter(
    private val view: OrdersContract.View,
    private val model: OrdersModel
) : OrdersContract.Presenter {

    override fun loadOrders() {
        view.showLoading()

        model.fetchMyOrders().enqueue(object : Callback<List<OrderModel>> {

            override fun onResponse(
                call: Call<List<OrderModel>>,
                response: Response<List<OrderModel>>
            ) {
                view.hideLoading()

                when {
                    response.isSuccessful -> {
                        val orders = response.body() ?: emptyList()
                        if (orders.isEmpty()) {
                            view.showEmpty("No orders yet.\nBrowse Services to get started!")
                        } else {
                            view.showOrders(orders)
                        }
                    }
                    response.code() == 401 || response.code() == 403 ->
                        view.showEmpty("Session expired or unauthorized. Please re-login.")
                    else ->
                        view.showError("Could not load orders (${response.code()}).")
                }
            }

            override fun onFailure(call: Call<List<OrderModel>>, t: Throwable) {
                view.hideLoading()
                view.showError("Cannot reach server. Make sure the backend is running.")
            }
        })
    }
}
