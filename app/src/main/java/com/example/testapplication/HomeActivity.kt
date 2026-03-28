package com.example.testapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
class HomeActivity : Activity() {

    lateinit var browseBtn: Button
    lateinit var navHome: ImageView
    lateinit var navOrders: ImageView
    lateinit var navSettings: ImageView
    lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        browseBtn = findViewById(R.id.browseServicesBtn)

        navHome = findViewById(R.id.navHome)
        navOrders = findViewById(R.id.navOrders)
        navSettings = findViewById(R.id.navSettings)

        navOrders.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }

        navSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        welcomeText = findViewById(R.id.welcomeText)
        
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val pref = getSharedPreferences("APP", MODE_PRIVATE)
        val token = pref.getString("TOKEN", null)

        if (token == null) {
            Toast.makeText(this, "Unauthorized, please login", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Temporarily bypassing the Dashboard API call so you can test Settings Activity freely.
        welcomeText.text = "Welcome!"
    }
}
