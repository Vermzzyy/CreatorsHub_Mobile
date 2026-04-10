package com.example.creatorshub.screens.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.creatorshub.R
import com.example.creatorshub.screens.register.RegisterActivity
import com.example.creatorshub.screens.settings.SettingsActivity
import com.example.creatorshub.data.SessionManager
import com.example.creatorshub.screens.login.LoginActivity

class MainActivity : Activity() {

    lateinit var loginBtn: Button
    lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)

        // ✅ Check if already logged in
        if (!session.getToken().isNullOrEmpty()) {
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