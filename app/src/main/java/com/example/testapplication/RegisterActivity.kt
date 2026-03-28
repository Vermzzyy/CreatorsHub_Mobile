package com.example.testapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.testapplication.api.RetrofitClient
import com.example.testapplication.models.RegisterRequest
import com.example.testapplication.models.RegisterResponse
import com.example.testapplication.models.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : Activity() {
    lateinit var loginLink: TextView
    lateinit var registerBtn: Button
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var confirmPasswordField: EditText
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginLink = findViewById(R.id.loginLink)
        registerBtn = findViewById(R.id.registerBtn)
        firstNameField = findViewById(R.id.firstName)
        lastNameField = findViewById(R.id.lastName)
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        confirmPasswordField = findViewById(R.id.confirmPassword)
        progressBar = findViewById(R.id.progressBar)

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        registerBtn.setOnClickListener {
            val fName = firstNameField.text.toString()
            val lName = lastNameField.text.toString()
            val email = emailField.text.toString()
            val pass = passwordField.text.toString()
            val confPass = confirmPasswordField.text.toString()

            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || pass.isEmpty() || confPass.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userData = UserData(fName, lName)
            val request = RegisterRequest(email, pass, userData)

            registerBtn.isEnabled = false
            progressBar.visibility = View.VISIBLE

            RetrofitClient.instance.register(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    registerBtn.isEnabled = true
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        when (response.code()) {
                            400 -> Toast.makeText(this@RegisterActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                            500 -> Toast.makeText(this@RegisterActivity, "Server error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(this@RegisterActivity, "Registration failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerBtn.isEnabled = true
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}