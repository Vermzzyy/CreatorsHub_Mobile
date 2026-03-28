package com.example.testapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {
    lateinit var loginBtn: Button
    lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        
        val pref = getSharedPreferences("APP", MODE_PRIVATE)
        if (pref.getString("TOKEN", null) != null) {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        loginBtn = findViewById(R.id.loginBtn)
        registerBtn = findViewById(R.id.registerBtn)

        loginBtn.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))

        }

        registerBtn.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))

        }

    }
}