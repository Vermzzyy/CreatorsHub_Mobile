package com.example.creatorshub.screens.services

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.creatorshub.R

/**
 * Activity to show specific service details (Demo Version).
 */
class ServiceDetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        val detailImage = findViewById<ImageView>(R.id.detailImage)
        
        // Load demo image for detail view
        Glide.with(this)
            .load("https://images.unsplash.com/photo-1460925895917-afdab827c52f?q=80&w=800")
            .into(detailImage)

        findViewById<TextView>(R.id.backBtn).setOnClickListener {
            onBackPressed()
        }

        findViewById<android.view.View>(R.id.logoutBtn).setOnClickListener {
            finish()
        }
    }
}
