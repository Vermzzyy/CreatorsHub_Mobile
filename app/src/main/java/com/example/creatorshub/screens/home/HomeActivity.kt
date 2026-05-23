package com.example.creatorshub.screens.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.creatorshub.R
import com.example.creatorshub.models.OrderModel
import com.example.creatorshub.models.ServiceResponse
import com.example.creatorshub.screens.login.LoginActivity
import com.example.creatorshub.screens.orders.OrdersActivity
import com.example.creatorshub.screens.services.ServiceDetailActivity
import com.example.creatorshub.screens.services.ServicesActivity
import com.example.creatorshub.screens.settings.SettingsActivity

class HomeActivity : Activity(), HomeContract.View {

    // ── Views ────────────────────────────────────────────────────────────────
    private lateinit var welcomeText: TextView
    private lateinit var browseBtn: TextView

    // Featured
    private lateinit var featuredProgress: ProgressBar
    private lateinit var featuredRow: LinearLayout
    private lateinit var featured1Card: CardView
    private lateinit var featured1Image: ImageView
    private lateinit var featured1Category: TextView
    private lateinit var featured1Name: TextView
    private lateinit var featured1Price: TextView
    private lateinit var featured2Card: CardView
    private lateinit var featured2Image: ImageView
    private lateinit var featured2Category: TextView
    private lateinit var featured2Name: TextView
    private lateinit var featured2Price: TextView

    // Orders section on home
    private lateinit var ordersHomeProgress: ProgressBar
    private lateinit var ordersHomeEmpty: TextView
    private lateinit var ordersHomeContainer: LinearLayout
    private lateinit var viewAllOrders: TextView

    // Store services list so cards can start detail intent
    private var featuredServices: List<ServiceResponse> = emptyList()

    private lateinit var presenter: HomePresenter

    // ── Lifecycle ────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        bindViews()
        wireNavigation()

        presenter = HomePresenter(this, HomeModel(), this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDashboard()
    }

    // ── View binding ─────────────────────────────────────────────────────────
    private fun bindViews() {
        welcomeText          = findViewById(R.id.welcomeText)
        browseBtn            = findViewById(R.id.browseServicesBtn)

        featuredProgress     = findViewById(R.id.featuredProgress)
        featuredRow          = findViewById(R.id.featuredRow)
        featured1Card        = findViewById(R.id.featured1Card)
        featured1Image       = findViewById(R.id.featured1Image)
        featured1Category    = findViewById(R.id.featured1Category)
        featured1Name        = findViewById(R.id.featured1Name)
        featured1Price       = findViewById(R.id.featured1Price)
        featured2Card        = findViewById(R.id.featured2Card)
        featured2Image       = findViewById(R.id.featured2Image)
        featured2Category    = findViewById(R.id.featured2Category)
        featured2Name        = findViewById(R.id.featured2Name)
        featured2Price       = findViewById(R.id.featured2Price)

        ordersHomeProgress   = findViewById(R.id.ordersHomeProgress)
        ordersHomeEmpty      = findViewById(R.id.ordersHomeEmpty)
        ordersHomeContainer  = findViewById(R.id.ordersHomeContainer)
        viewAllOrders        = findViewById(R.id.viewAllOrders)
    }

    // ── Navigation wiring ────────────────────────────────────────────────────
    private fun wireNavigation() {
        // Bottom navbar
        findViewById<ImageView>(R.id.navHome).setOnClickListener { /* already here */ }
        findViewById<ImageView>(R.id.navOrders).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }
        findViewById<ImageView>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Hero buttons
        browseBtn.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
        }
        viewAllOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }
    }

    // ── HomeContract.View implementation ─────────────────────────────────────

    override fun showWelcome(name: String) {
        welcomeText.text = name
    }

    // Featured
    override fun showFeaturedLoading() {
        featuredProgress.visibility = View.VISIBLE
        featuredRow.visibility      = View.GONE
    }

    override fun hideFeaturedLoading() {
        featuredProgress.visibility = View.GONE
    }

    override fun showFeaturedServices(services: List<ServiceResponse>) {
        featuredServices = services
        featuredRow.visibility = View.VISIBLE

        if (services.isNotEmpty()) {
            bindFeaturedCard(
                services[0],
                featured1Card, featured1Image,
                featured1Category, featured1Name, featured1Price
            )
        }
        if (services.size >= 2) {
            bindFeaturedCard(
                services[1],
                featured2Card, featured2Image,
                featured2Category, featured2Name, featured2Price
            )
        } else {
            featured2Card.visibility = View.GONE
        }
    }

    private fun bindFeaturedCard(
        service: ServiceResponse,
        card: CardView,
        imageView: ImageView,
        categoryView: TextView,
        nameView: TextView,
        priceView: TextView
    ) {
        categoryView.text = service.category ?: "Misc"
        nameView.text     = service.title ?: "Untitled"
        priceView.text    = service.price ?: "N/A"

        if (!service.thumbnail.isNullOrEmpty()) {
            Glide.with(this)
                .load(service.thumbnail)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(imageView)
        }

        card.setOnClickListener {
            val intent = Intent(this, ServiceDetailActivity::class.java).apply {
                putExtra("SERVICE_ID",          service.id)
                putExtra("SERVICE_TITLE",       service.title ?: "Untitled")
                putExtra("SERVICE_CATEGORY",    service.category ?: "Misc")
                putExtra("SERVICE_PRICE",       service.price ?: "N/A")
                putExtra("SERVICE_DESCRIPTION", service.description ?: "")
                putExtra("SERVICE_THUMBNAIL",   service.thumbnail)
                putStringArrayListExtra("SERVICE_TAGS", ArrayList(service.tags ?: emptyList()))
            }
            startActivity(intent)
        }
    }

    // Orders
    override fun showOrdersLoading() {
        ordersHomeProgress.visibility  = View.VISIBLE
        ordersHomeEmpty.visibility     = View.GONE
        ordersHomeContainer.visibility = View.GONE
    }

    override fun hideOrdersLoading() {
        ordersHomeProgress.visibility = View.GONE
    }

    override fun showRecentOrders(orders: List<OrderModel>) {
        ordersHomeContainer.visibility = View.VISIBLE
        ordersHomeContainer.removeAllViews()
        orders.forEach { addOrderCard(it) }
    }

    override fun showOrdersEmpty(message: String) {
        ordersHomeEmpty.text       = message
        ordersHomeEmpty.visibility = View.VISIBLE
    }

    // General
    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Inflates a compact order card programmatically into the home orders container. */
    private fun addOrderCard(order: OrderModel) {
        val dp = resources.displayMetrics.density

        val card = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.bottomMargin = (10 * dp).toInt() }
            radius        = 10f * dp
            cardElevation = 3f * dp
            setCardBackgroundColor(androidx.core.content.ContextCompat.getColor(this@HomeActivity, R.color.surface_white))
        }

        val inner = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (14 * dp).toInt()
            setPadding(pad, pad, pad, pad)
        }

        // Title + status row
        val topRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity     = android.view.Gravity.CENTER_VERTICAL
        }

        val titleView = TextView(this).apply {
            text      = order.serviceTitle ?: "Untitled Service"
            textSize  = 14f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(androidx.core.content.ContextCompat.getColor(this@HomeActivity, R.color.text_primary))
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            maxLines  = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }

        val badgeText = order.status ?: "PENDING"
        val badge = TextView(this).apply {
            text     = badgeText
            textSize = 10f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(Color.WHITE)
            val bgColor = when (badgeText.uppercase()) {
                "COMPLETED"   -> Color.parseColor("#2E7D32")
                "IN_PROGRESS" -> Color.parseColor("#1565C0")
                "CANCELLED"   -> Color.parseColor("#B71C1C")
                else          -> Color.parseColor("#E65100")
            }
            setBackgroundColor(bgColor)
            val px = (10 * dp).toInt(); val py = (4 * dp).toInt()
            setPadding(px, py, px, py)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.marginStart = (8 * dp).toInt() }
        }

        topRow.addView(titleView)
        topRow.addView(badge)

        val priceView = TextView(this).apply {
            text      = order.price ?: "N/A"
            textSize  = 12f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(androidx.core.content.ContextCompat.getColor(this@HomeActivity, R.color.accent_red))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = (4 * dp).toInt() }
        }

        val dateRaw = order.createdAt ?: ""
        val dateStr = if (dateRaw.length >= 10) dateRaw.substring(0, 10) else dateRaw
        val dateView = TextView(this).apply {
            text     = dateStr
            textSize = 11f
            setTextColor(androidx.core.content.ContextCompat.getColor(this@HomeActivity, R.color.text_hint))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.topMargin = (4 * dp).toInt() }
        }

        inner.addView(topRow)
        inner.addView(priceView)
        inner.addView(dateView)
        card.addView(inner)
        ordersHomeContainer.addView(card)
    }
}