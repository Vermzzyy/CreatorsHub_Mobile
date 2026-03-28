package com.example.testapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.models.ServiceModel

class ServiceAdapter(private val services: List<ServiceModel>) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceImage: ImageView = itemView.findViewById(R.id.serviceImage)
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)

        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {

        val service = services[position]

        holder.serviceName.text = service.name
        holder.serviceImage.setImageResource(service.image)
    }

    override fun getItemCount(): Int {
        return services.size
    }
}