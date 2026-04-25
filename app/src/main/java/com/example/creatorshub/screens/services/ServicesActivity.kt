package com.example.creatorshub.screens.services

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorshub.R
import com.example.creatorshub.adapters.ServiceAdapter
import com.example.creatorshub.models.ServiceModel

/**
 * ServicesActivity using findViewById for compatibility.
 */
class ServicesActivity : Activity(), ServicesContract.View {
    
    private lateinit var presenter: ServicesPresenter
    private lateinit var recyclerView: RecyclerView
    private var serviceAdapter: ServiceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        recyclerView = findViewById(R.id.servicesRecycler)
        presenter = ServicesPresenter(this, ServicesModel())
        
        setupRecyclerView()
        presenter.loadServices()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    override fun showServices(services: List<ServiceModel>) {
        serviceAdapter = ServiceAdapter(ArrayList(services))
        recyclerView.adapter = serviceAdapter
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}