package com.example.creatorshub.screens.home

import android.content.Context
import com.example.creatorshub.shared.SessionManager

class HomePresenter(
    private val view: HomeContract.View,
    private val model: HomeModel,
    private val context: Context
) : HomeContract.Presenter {

    override fun loadDashboard() {
        val session = SessionManager(context)
        val token = session.getToken()

        if (token.isNullOrEmpty()) {
            view.showError("Unauthorized, please login")
            view.navigateToLogin()
            return
        }

        model.fetchProfile { firstName ->
            val name = firstName ?: "User"
            view.showWelcome("Welcome, $name!")
        }
    }
}