package com.example.creatorshub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.creatorshub.R
import com.example.creatorshub.models.ServiceModel

class ServiceAdapter(
    private val services: List<ServiceModel>,
    private val onItemClick: (ServiceModel) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceImage: ImageView = itemView.findViewById(R.id.serviceImage)
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceCategory: TextView = itemView.findViewById(R.id.serviceCategory)
        val servicePrice: TextView = itemView.findViewById(R.id.servicePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]

        holder.serviceName.text = service.name
        holder.serviceCategory.text = service.category
        holder.servicePrice.text = service.price

        // Load thumbnail from URL using Glide, fallback to placeholder drawable
        if (!service.thumbnailUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(service.thumbnailUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(holder.serviceImage)
        } else {
            holder.serviceImage.setImageResource(R.drawable.placeholder)
        }

        holder.itemView.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount(): Int = services.size
}