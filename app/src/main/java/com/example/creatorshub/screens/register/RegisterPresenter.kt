package com.example.creatorshub.screens.register

import com.example.creatorshub.screens.register.model.RegisterRequest
import com.example.creatorshub.screens.register.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val model: RegisterModel
) : RegisterContract.Presenter {

    override fun register(
        fName: String,
        lName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {

        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()
            || password.isEmpty() || confirmPassword.isEmpty()
        ) {
            view.showError("Fill all fields")
            return
        }

        if (password != confirmPassword) {
            view.showError("Passwords do not match")
            return
        }

        view.disableButton()
        view.showLoading()

        // ✅ Supabase expects first_name/last_name inside a "data" object (user_metadata)
        val request = RegisterRequest(
            email = email,
            password = password,
            data = RegisterRequest.UserData(
                first_name = fName,
                last_name = lName
            )
        )

        model.register(request).enqueue(object : Callback<RegisterResponse> {

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                view.enableButton()
                view.hideLoading()

                if (response.isSuccessful) {
                    view.showSuccess("Registration Successful! Please log in.")
                    view.navigateToLogin()
                } else {
                    when (response.code()) {
                        400 -> view.showError("Invalid data. Check your email and password.")
                        422 -> view.showError("Email already registered.")
                        500 -> view.showError("Server error. Try again later.")
                        else -> view.showError("Registration failed (${response.code()})")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                view.enableButton()
                view.hideLoading()
                view.showError("No internet connection")
            }
        })
    }
}