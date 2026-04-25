package com.example.creatorshub.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.creatorshub.R
import com.example.creatorshub.models.ServiceModel
import com.example.creatorshub.screens.services.ServiceDetailActivity

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
        
        // Using Glide to load online images
        Glide.with(holder.itemView.context)
            .load(service.imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.serviceImage)

        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, ServiceDetailActivity::class.java)
            // Transferring URL to detail activity (optional, for demo we just open it)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return services.size
    }
}