package com.example.creatorshub.screens.services

import com.example.creatorshub.models.ServiceResponse

interface ServicesContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showServices(services: List<ServiceResponse>)
        fun showEmpty(message: String)
        fun showError(message: String)
        fun openServiceDetail(service: ServiceResponse)
    }

    interface Presenter {
        fun loadServices()
    }
}
