package com.example.creatorshub.screens.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.creatorshub.R
import com.example.creatorshub.shared.SessionManager
import com.example.creatorshub.screens.home.HomeActivity
import com.example.creatorshub.screens.login.LoginActivity
import com.example.creatorshub.screens.register.RegisterActivity

/**
 * MainActivity refactored to use findViewById (Compatibility Mode).
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val session = SessionManager(this)
        if (!session.getToken().isNullOrEmpty()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.loginBtn).setOnClickListener { 
            startActivity(Intent(this, LoginActivity::class.java)) 
        }
        findViewById<Button>(R.id.registerBtn).setOnClickListener { 
            startActivity(Intent(this, RegisterActivity::class.java)) 
        }
    }
}