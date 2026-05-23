package com.example.creatorshub.screens.services

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorshub.R
import com.example.creatorshub.adapters.ServiceAdapter
import com.example.creatorshub.data.BackendClient
import com.example.creatorshub.models.ServiceModel
import com.example.creatorshub.models.ServiceResponse
import com.example.creatorshub.screens.home.HomeActivity
import com.example.creatorshub.screens.settings.SettingsActivity

class ServicesActivity : Activity(), ServicesContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyContainer: LinearLayout
    private lateinit var emptyText: TextView

    private lateinit var chipAll: TextView
    private lateinit var chipUiUx: TextView
    private lateinit var chipGameDev: TextView
    private lateinit var chipGraphicDesign: TextView

    private var allServices: List<ServiceResponse> = emptyList()

    private lateinit var presenter: ServicesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        recyclerView   = findViewById(R.id.servicesRecycler)
        progressBar    = findViewById(R.id.servicesProgress)
        emptyContainer = findViewById(R.id.servicesEmptyContainer)
        emptyText      = findViewById(R.id.servicesEmpty)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // ── Bottom navigation ────────────────────────────────────────────────
        findViewById<ImageView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.navOrders).setOnClickListener { /* already on services */ }
        findViewById<ImageView>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // ── Categories ───────────────────────────────────────────────────────
        chipAll           = findViewById(R.id.chipAll)
        chipUiUx          = findViewById(R.id.chipUiUx)
        chipGameDev       = findViewById(R.id.chipGameDev)
        chipGraphicDesign = findViewById(R.id.chipGraphicDesign)

        setupChipListeners()

        // ── Presenter ────────────────────────────────────────────────────────
        presenter = ServicesPresenter(this, ServicesModel(BackendClient.create(this)))
    }

    override fun onResume() {
        super.onResume()
        presenter.loadServices()
    }

    private fun setupChipListeners() {
        chipAll.setOnClickListener { selectCategory(chipAll, null) }
        chipUiUx.setOnClickListener { selectCategory(chipUiUx, "UI/UX") }
        chipGameDev.setOnClickListener { selectCategory(chipGameDev, "Game Dev") }
        chipGraphicDesign.setOnClickListener { selectCategory(chipGraphicDesign, "Graphic Design") }
    }

    private fun selectCategory(selectedChip: TextView, category: String?) {
        // Reset all chips
        val chips = listOf(chipAll, chipUiUx, chipGameDev, chipGraphicDesign)
        for (chip in chips) {
            chip.setBackgroundResource(R.drawable.input_field)
            chip.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary))
        }

        // Highlight selected chip
        selectedChip.setBackgroundResource(R.drawable.primary_button)
        selectedChip.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white))

        // Filter list
        val filtered = if (category == null) {
            allServices
        } else {
            allServices.filter { it.category?.contains(category, ignoreCase = true) == true }
        }
        
        if (filtered.isEmpty()) {
            showEmpty("No services found for this category.")
        } else {
            emptyContainer.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            updateAdapter(filtered)
        }
    }

    // ── ServicesContract.View ────────────────────────────────────────────────

    override fun showLoading() {
        progressBar.visibility    = View.VISIBLE
        emptyContainer.visibility = View.GONE
        recyclerView.visibility   = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showServices(services: List<ServiceResponse>) {
        // Store full list of services when loaded from API
        allServices = services
        
        recyclerView.visibility = View.VISIBLE
        updateAdapter(services)
    }

    private fun updateAdapter(services: List<ServiceResponse>) {
        val serviceModels = services.map { s ->
            ServiceModel(
                id = s.id,
                name = s.title ?: "Untitled",
                category = s.category ?: "Misc",
                price = s.price ?: "N/A",
                thumbnailUrl = s.thumbnail
            )
        }
        recyclerView.adapter = ServiceAdapter(serviceModels) { selected ->
            openServiceDetail(allServices.first { it.id == selected.id })
        }
    }

    override fun showEmpty(message: String) {
        emptyText.text            = message
        emptyContainer.visibility = View.VISIBLE
    }

    override fun showError(message: String) {
        emptyText.text            = message
        emptyContainer.visibility = View.VISIBLE
    }

    override fun openServiceDetail(service: ServiceResponse) {
        val intent = Intent(this, ServiceDetailActivity::class.java).apply {
            putExtra("SERVICE_ID",          service.id)
            putExtra("SERVICE_TITLE",       service.title)
            putExtra("SERVICE_CATEGORY",    service.category)
            putExtra("SERVICE_PRICE",       service.price)
            putExtra("SERVICE_DESCRIPTION", service.description)
            putExtra("SERVICE_THUMBNAIL",   service.thumbnail)
            putStringArrayListExtra("SERVICE_TAGS", ArrayList(service.tags))
        }
        startActivity(intent)
    }
}