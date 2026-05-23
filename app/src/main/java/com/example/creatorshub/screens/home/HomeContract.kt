package com.example.creatorshub.screens.home

import com.example.creatorshub.models.OrderModel
import com.example.creatorshub.models.ServiceResponse

interface HomeContract {

    interface View {
        // Profile
        fun showWelcome(name: String)

        // Featured services (first 2 from API)
        fun showFeaturedLoading()
        fun showFeaturedServices(services: List<ServiceResponse>)
        fun hideFeaturedLoading()

        // Orders on home (recent 2)
        fun showOrdersLoading()
        fun showRecentOrders(orders: List<OrderModel>)
        fun showOrdersEmpty(message: String)
        fun hideOrdersLoading()

        // Navigation / errors
        fun navigateToLogin()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadDashboard()
    }
}