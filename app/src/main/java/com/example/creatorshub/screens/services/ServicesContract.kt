package com.example.creatorshub.screens.services

import com.example.creatorshub.models.ServiceModel

/**
 * MVP Contract for Services Screen
 */
interface ServicesContract {
    interface View {
        fun showServices(services: List<ServiceModel>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadServices()
    }

    interface Model {
        fun getServices(): List<ServiceModel>
    }
}
