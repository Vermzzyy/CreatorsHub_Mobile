package com.example.creatorshub.screens.orders

import com.example.creatorshub.data.BackendApiService
import com.example.creatorshub.models.OrderModel
import retrofit2.Call

class OrdersModel(private val client: BackendApiService) {

    fun fetchMyOrders(): Call<List<OrderModel>> {
        return client.getMyOrders()
    }
}
