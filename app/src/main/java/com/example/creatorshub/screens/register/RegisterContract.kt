package com.example.creatorshub.screens.register

interface RegisterContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showSuccess(message: String)
        fun enableButton()
        fun disableButton()
        fun navigateToLogin()
    }

    interface Presenter {
        fun register(
            fName: String,
            lName: String,
            email: String,
            password: String,
            confirmPassword: String
        )
    }
}