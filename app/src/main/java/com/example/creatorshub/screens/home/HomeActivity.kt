package com.example.creatorshub.screens.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.creatorshub.R
import com.example.creatorshub.shared.RetrofitClient
import com.example.creatorshub.screens.services.ServicesActivity
import com.example.creatorshub.screens.settings.SettingsActivity
import com.example.creatorshub.screens.login.LoginActivity

class HomeActivity : Activity(), HomeContract.View {

    lateinit var browseBtn: Button
    lateinit var navHome: ImageView
    lateinit var navOrders: ImageView
    lateinit var navSettings: ImageView
    lateinit var welcomeText: TextView

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        browseBtn = findViewById(R.id.browseServicesBtn)
        navHome = findViewById(R.id.navHome)
        navOrders = findViewById(R.id.navOrders)
        navSettings = findViewById(R.id.navSettings)
        welcomeText = findViewById(R.id.welcomeText)

        // Initialize API and Presenter
        val api = RetrofitClient.create(this)
        presenter = HomePresenter(this, HomeModel(api), this)

        // Navigation listeners
        browseBtn.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }

        navOrders.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }

        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        presenter.loadDashboard()
    }

    override fun showWelcome(message: String) {
        welcomeText.text = message
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}