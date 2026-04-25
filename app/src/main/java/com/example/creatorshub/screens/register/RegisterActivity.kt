package com.example.creatorshub.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.creatorshub.R
import com.example.creatorshub.shared.RetrofitClient
import com.example.creatorshub.screens.login.LoginActivity

class RegisterActivity : Activity(), RegisterContract.View {

    lateinit var loginLink: TextView
    lateinit var registerBtn: Button
    lateinit var firstNameField: EditText
    lateinit var lastNameField: EditText
    lateinit var emailField: EditText
    lateinit var passwordField: EditText
    lateinit var confirmPasswordField: EditText
    lateinit var progressBar: ProgressBar

    private lateinit var presenter: RegisterPresenter

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

        // ✅ Use Auth client for register — targets /auth/v1/signup
        val authApi = RetrofitClient.createAuthClient()

        presenter = RegisterPresenter(
            this,
            RegisterModel(authApi)
        )

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        registerBtn.setOnClickListener {
            presenter.register(
                firstNameField.text.toString().trim(),
                lastNameField.text.toString().trim(),
                emailField.text.toString().trim(),
                passwordField.text.toString(),
                confirmPasswordField.text.toString()
            )
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

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun enableButton() {
        registerBtn.isEnabled = true
    }

    override fun disableButton() {
        registerBtn.isEnabled = false
    }
}