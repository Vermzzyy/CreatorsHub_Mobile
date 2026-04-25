package com.example.creatorshub.screens.services

/**
 * Presenter implementation for Services Screen
 */
class ServicesPresenter(
    private var view: ServicesContract.View?,
    private val model: ServicesContract.Model
) : ServicesContract.Presenter {

    override fun loadServices() {
        try {
            val services = model.getServices()
            view?.showServices(services)
        } catch (e: Exception) {
            view?.showError("Failed to load services")
        }
    }

    fun detachView() {
        view = null
    }
}
