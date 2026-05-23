package com.example.creatorshub.screens.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorshub.R
import com.example.creatorshub.adapters.OrderAdapter
import com.example.creatorshub.data.BackendClient
import com.example.creatorshub.models.OrderModel
import com.example.creatorshub.screens.home.HomeActivity
import com.example.creatorshub.screens.services.ServicesActivity
import com.example.creatorshub.screens.settings.SettingsActivity

class OrdersActivity : Activity(), OrdersContract.View {

    private lateinit var ordersRecycler: RecyclerView
    private lateinit var ordersProgress: ProgressBar
    private lateinit var ordersEmptyContainer: LinearLayout
    private lateinit var ordersEmpty: TextView

    private lateinit var presenter: OrdersPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        ordersRecycler       = findViewById(R.id.ordersRecycler)
        ordersProgress       = findViewById(R.id.ordersProgress)
        ordersEmptyContainer = findViewById(R.id.ordersEmptyContainer)
        ordersEmpty          = findViewById(R.id.ordersEmpty)

        ordersRecycler.layoutManager = LinearLayoutManager(this)

        // ── Bottom navigation ────────────────────────────────────────────────
        findViewById<ImageView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navOrders).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // ── Presenter ────────────────────────────────────────────────────────
        presenter = OrdersPresenter(this, OrdersModel(BackendClient.create(this)))
        presenter.loadOrders()
    }

    // ── OrdersContract.View ──────────────────────────────────────────────────

    override fun showLoading() {
        ordersProgress.visibility       = View.VISIBLE
        ordersEmptyContainer.visibility = View.GONE
        ordersRecycler.visibility       = View.GONE
    }

    override fun hideLoading() {
        ordersProgress.visibility = View.GONE
    }

    override fun showOrders(orders: List<OrderModel>) {
        ordersRecycler.adapter    = OrderAdapter(orders)
        ordersRecycler.visibility = View.VISIBLE
    }

    override fun showEmpty(message: String) {
        ordersEmpty.text                = message
        ordersEmptyContainer.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        ordersEmpty.text                = message
        ordersEmptyContainer.visibility = View.VISIBLE
    }
}
