package com.example.creatorshub.screens.login

import android.content.Context
import com.example.creatorshub.data.BackendClient
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
                    val token = body?.access_token
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


                    loginToBackend(email, password, session) {
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

    private fun loginToBackend(email: String, password: String, session: SessionManager, onComplete: () -> Unit) {
        try {
            val backendAuth = BackendClient.createAuthClient(context)
            val backendRequest = mapOf("email" to email, "password" to password)
            backendAuth.login(backendRequest).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        val t = response.body()?.get("token") ?: response.body()?.get("accessToken") ?: response.body()?.get("jwt")
                        if (t != null) {
                            session.saveBackendToken(t.toString())
                        }
                        onComplete()
                    } else if (response.code() == 401 || response.code() == 404 || response.code() == 500) {
                        val regData = mapOf(
                            "email" to email, 
                            "password" to password,
                            "confirmPassword" to password,
                            "firstName" to email.substringBefore("@"),
                            "lastName" to "User"
                        )
                        val loginData = mapOf("email" to email, "password" to password)
                        backendAuth.register(regData).enqueue(object : Callback<Map<String, Any>> {
                            override fun onResponse(call: Call<Map<String, Any>>, resp: Response<Map<String, Any>>) {
                                backendAuth.login(loginData).enqueue(object : Callback<Map<String, Any>> {
                                    override fun onResponse(c: Call<Map<String, Any>>, r: Response<Map<String, Any>>) {
                                        if (r.isSuccessful) {
                                            val t2 = r.body()?.get("token") ?: r.body()?.get("accessToken") ?: r.body()?.get("jwt")
                                            if (t2 != null) {
                                                session.saveBackendToken(t2.toString())
                                            }
                                        }
                                        onComplete()
                                    }
                                    override fun onFailure(c: Call<Map<String, Any>>, t: Throwable) {
                                        onComplete()
                                    }
                                })
                            }
                            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                                onComplete()
                            }
                        })
                    } else {
                        onComplete()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    onComplete()
                }
            })
        } catch (e: Exception) {
            onComplete()
        }
    }
}