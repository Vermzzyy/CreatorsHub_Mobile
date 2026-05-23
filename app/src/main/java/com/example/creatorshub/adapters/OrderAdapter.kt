package com.example.creatorshub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorshub.R
import com.example.creatorshub.models.OrderModel

class OrderAdapter(private val orders: List<OrderModel>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderTitle: TextView        = itemView.findViewById(R.id.orderTitle)
        val orderStatus: TextView       = itemView.findViewById(R.id.orderStatus)
        val orderPrice: TextView        = itemView.findViewById(R.id.orderPrice)
        val orderInstructions: TextView = itemView.findViewById(R.id.orderInstructions)
        val orderDate: TextView         = itemView.findViewById(R.id.orderDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.orderTitle.text = order.serviceTitle ?: "Untitled Service"
        holder.orderPrice.text = order.price ?: "N/A"

        // Status badge colour
        val statusText = order.status ?: "PENDING"
        holder.orderStatus.text = statusText
        val bgColor = when (statusText.uppercase()) {
            "COMPLETED"   -> 0xFF2E7D32.toInt()   // green
            "IN_PROGRESS" -> 0xFF1565C0.toInt()   // blue
            "CANCELLED"   -> 0xFFB71C1C.toInt()   // red
            else          -> 0xFFE65100.toInt()   // orange = PENDING
        }
        holder.orderStatus.setBackgroundColor(bgColor)

        // Instructions (hide if empty)
        if (!order.instructions.isNullOrEmpty()) {
            holder.orderInstructions.text       = "\"${order.instructions}\""
            holder.orderInstructions.visibility = View.VISIBLE
        } else {
            holder.orderInstructions.visibility = View.GONE
        }

        // Date — trim to date portion if ISO-8601 datetime is returned
        val dateRaw = order.createdAt ?: ""
        holder.orderDate.text = if (dateRaw.length >= 10)
            dateRaw.substring(0, 10)
        else
            dateRaw
    }

    override fun getItemCount(): Int = orders.size
}
