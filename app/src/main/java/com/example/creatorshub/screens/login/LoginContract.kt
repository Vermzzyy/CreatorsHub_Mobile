package com.example.creatorshub.screens.login

interface LoginContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateToDashboard()
        fun enableButton()
        fun disableButton()
    }

    interface Presenter {
        fun login(email: String, password: String)
    }
}