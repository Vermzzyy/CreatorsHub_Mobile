package com.example.creatorshub.screens.home

interface HomeContract {

    interface View {
        fun showWelcome(message: String)
        fun navigateToLogin()
        fun showError(message: String)
    }

    interface Presenter {
        fun loadDashboard()
    }
}