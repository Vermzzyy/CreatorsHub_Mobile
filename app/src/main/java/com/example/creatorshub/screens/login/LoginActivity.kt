package com.example.creatorshub.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.creatorshub.screens.home.HomeActivity
import com.example.creatorshub.R
import com.example.creatorshub.screens.register.RegisterActivity
import com.example.creatorshub.data.RetrofitClient

class LoginActivity : Activity(), LoginContract.View {

    lateinit var signupLink: TextView
    lateinit var loginBtn: Button
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var progressBar: ProgressBar

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        signupLink = findViewById(R.id.signupLink)
        loginBtn = findViewById(R.id.loginBtn)
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        progressBar = findViewById(R.id.progressBar)

        // ✅ Use Auth client for login — targets /auth/v1/token
        val authApi = RetrofitClient.createAuthClient()

        presenter = LoginPresenter(
            this,
            LoginModel(authApi),
            this
        )

        signupLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            presenter.login(email, password)
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToDashboard() {
        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun enableButton() {
        loginBtn.isEnabled = true
    }

    override fun disableButton() {
        loginBtn.isEnabled = false
    }
}