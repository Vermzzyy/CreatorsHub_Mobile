package com.example.creatorshub.screens.services

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorshub.adapters.ServiceAdapter
import com.example.creatorshub.models.ServiceModel
import com.example.creatorshub.R

class ServicesActivity : Activity() {
    lateinit var recyclerView: RecyclerView
    lateinit var serviceAdapter: ServiceAdapter
    lateinit var serviceList: ArrayList<ServiceModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        recyclerView = findViewById(R.id.servicesRecycler)

        serviceList = arrayListOf(
            ServiceModel("Website UI", R.drawable.placeholder),
            ServiceModel("App UI", R.drawable.placeholder),
            ServiceModel("Roblox Scripting", R.drawable.placeholder),
            ServiceModel("Poster", R.drawable.placeholder),
            ServiceModel("3D Animation", R.drawable.placeholder),
            ServiceModel("Logo Design", R.drawable.placeholder),
            ServiceModel("3D Modelling", R.drawable.placeholder),
            ServiceModel("Web Development", R.drawable.placeholder)
        )

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        serviceAdapter = ServiceAdapter(serviceList)
        recyclerView.adapter = serviceAdapter
    }
}