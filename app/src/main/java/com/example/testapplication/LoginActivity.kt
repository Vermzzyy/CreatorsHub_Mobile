package com.example.testapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.testapplication.api.RetrofitClient
import com.example.testapplication.models.LoginRequest
import com.example.testapplication.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {

    lateinit var signupLink: TextView
    lateinit var loginBtn: Button
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var progressBar: android.widget.ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        signupLink = findViewById(R.id.signupLink)
        loginBtn = findViewById(R.id.loginBtn)
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        progressBar = findViewById(R.id.progressBar)

        signupLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {

            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(email, password)

            loginBtn.isEnabled = false // disable button
            progressBar.visibility = android.view.View.VISIBLE

            RetrofitClient.instance.login(request)
                .enqueue(object: Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        loginBtn.isEnabled = true
                        progressBar.visibility = android.view.View.GONE

                        if (response.isSuccessful) {

                            val token = response.body()?.token

                            // SAVE TOKEN
                            val pref = getSharedPreferences("APP", MODE_PRIVATE)
                            pref.edit().putString("TOKEN", token).apply()

                            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@LoginActivity, SettingsActivity::class.java))
                            finish()

                        } else {

                            when (response.code()) {
                                400 -> Toast.makeText(this@LoginActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                                401 -> Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                                500 -> Toast.makeText(this@LoginActivity, "Server error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                                else -> Toast.makeText(this@LoginActivity, "Login failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        loginBtn.isEnabled = true
                        progressBar.visibility = android.view.View.GONE
                        Toast.makeText(this@LoginActivity, "Network Error: No internet connection or API failure", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
