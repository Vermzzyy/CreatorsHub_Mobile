package com.example.creatorshub.screens.orders

import com.example.creatorshub.models.OrderModel

interface OrdersContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showOrders(orders: List<OrderModel>)
        fun showEmpty(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadOrders()
    }
}
