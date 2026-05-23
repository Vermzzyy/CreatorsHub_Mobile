package com.example.creatorshub.screens.services

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.creatorshub.R
import com.example.creatorshub.data.BackendClient

class ServiceDetailActivity : Activity(), ServiceDetailContract.View {

    private lateinit var detailImage: ImageView
    private lateinit var detailCategory: TextView
    private lateinit var detailTitle: TextView
    private lateinit var detailPrice: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailTagsLabel: TextView
    private lateinit var detailTags: TextView
    private lateinit var detailInstructions: EditText
    private lateinit var detailBookBtn: Button
    private lateinit var detailBookStatus: TextView

    private lateinit var presenter: ServiceDetailPresenter
    private var serviceId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        // ── Bind views ───────────────────────────────────────────────────────
        detailImage        = findViewById(R.id.detailImage)
        detailCategory     = findViewById(R.id.detailCategory)
        detailTitle        = findViewById(R.id.detailTitle)
        detailPrice        = findViewById(R.id.detailPrice)
        detailDescription  = findViewById(R.id.detailDescription)
        detailTagsLabel    = findViewById(R.id.detailTagsLabel)
        detailTags         = findViewById(R.id.detailTags)
        detailInstructions = findViewById(R.id.detailInstructions)
        detailBookBtn      = findViewById(R.id.detailBookBtn)
        detailBookStatus   = findViewById(R.id.detailBookStatus)

        // ── Populate from Intent extras ──────────────────────────────────────
        serviceId               = intent.getLongExtra("SERVICE_ID", -1L)
        detailTitle.text        = intent.getStringExtra("SERVICE_TITLE") ?: ""
        detailCategory.text     = intent.getStringExtra("SERVICE_CATEGORY") ?: ""
        detailPrice.text        = intent.getStringExtra("SERVICE_PRICE") ?: ""
        detailDescription.text  = intent.getStringExtra("SERVICE_DESCRIPTION") ?: ""

        val tags = intent.getStringArrayListExtra("SERVICE_TAGS") ?: arrayListOf()
        if (tags.isNotEmpty()) {
            detailTagsLabel.visibility = View.VISIBLE
            detailTags.visibility      = View.VISIBLE
            detailTags.text            = tags.joinToString("  •  ")
        }

        val thumbnail = intent.getStringExtra("SERVICE_THUMBNAIL")
        if (!thumbnail.isNullOrEmpty()) {
            Glide.with(this)
                .load(thumbnail)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(detailImage)
        }

        // ── Presenter ────────────────────────────────────────────────────────
        presenter = ServiceDetailPresenter(
            this,
            ServiceDetailModel(BackendClient.create(this))
        )

        detailBookBtn.setOnClickListener {
            presenter.bookService(serviceId, detailInstructions.text.toString().trim())
        }

        findViewById<View>(R.id.detailBackBtn).setOnClickListener {
            onBackPressed()
        }
    }

    // ── ServiceDetailContract.View ───────────────────────────────────────────

    override fun showBookingLoading() {
        detailBookStatus.text       = "Booking..."
        detailBookStatus.setTextColor(0xFF888888.toInt())
        detailBookStatus.visibility = View.VISIBLE
    }

    override fun showBookingSuccess() {
        detailBookStatus.text       = "✅ Booked successfully! Check My Orders on Home."
        detailBookStatus.setTextColor(0xFF2E7D32.toInt())
        detailBookStatus.visibility = View.VISIBLE
        detailInstructions.text.clear()
    }

    override fun showBookingError(message: String) {
        detailBookStatus.text       = message
        detailBookStatus.setTextColor(0xFFB71C1C.toInt())
        detailBookStatus.visibility = View.VISIBLE
    }

    override fun enableBookButton()  { detailBookBtn.isEnabled = true }
    override fun disableBookButton() { detailBookBtn.isEnabled = false }
}
