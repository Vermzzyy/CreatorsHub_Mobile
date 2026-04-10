package com.example.creatorshub.screens.login

import android.content.Context
import com.example.creatorshub.data.SessionManager
import com.example.creatorshub.screens.login.model.LoginRequest
import com.example.creatorshub.screens.login.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel,
    private val context: Context
) : LoginContract.Presenter {

    override fun login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Fill all fields")
            return
        }

        view.disableButton()
        view.showLoading()

        val request = LoginRequest(email, password)

        model.login(request).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                view.enableButton()
                view.hideLoading()

                if (response.isSuccessful) {

                    val body = response.body()
                    val token = body?.access_token   // ✅ Supabase returns "access_token"
                    val userId = body?.user?.id

                    if (token.isNullOrEmpty()) {
                        view.showError("Login failed: no token received")
                        return
                    }

                    val session = SessionManager(context)
                    session.saveToken(token)
                    if (!userId.isNullOrEmpty()) {
                        session.saveUserId(userId)
                    }

                    view.navigateToDashboard()

                } else {
                    when (response.code()) {
                        400 -> view.showError("Invalid email or password")
                        401 -> view.showError("Unauthorized")
                        422 -> view.showError("Invalid email or password")
                        500 -> view.showError("Server error. Try again later.")
                        else -> view.showError("Login failed (${response.code()})")
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                view.enableButton()
                view.hideLoading()
                view.showError("No internet connection")
            }
        })
    }
}