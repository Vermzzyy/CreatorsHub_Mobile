package com.example.creatorshub.screens.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.creatorshub.R
import com.example.creatorshub.data.RetrofitClient
import com.example.creatorshub.screens.login.LoginActivity
import com.example.creatorshub.models.ProfileResponse
import com.example.creatorshub.screens.home.HomeActivity
import com.example.creatorshub.screens.settings.*

class SettingsActivity : Activity(), SettingsContract.View {

    lateinit var logoutBtn: Button
    lateinit var editProfile: LinearLayout
    lateinit var changePassword: LinearLayout
    lateinit var faq: LinearLayout
    lateinit var contact: LinearLayout
    lateinit var navHome: ImageView
    lateinit var navOrders: ImageView
    lateinit var navSettings: ImageView
    lateinit var profileImage: ImageView
    lateinit var profileName: TextView
    lateinit var profileEmail: TextView

    private var currentUserId: String? = null
    private var currentFirstName: String = ""
    private var currentLastName: String = ""

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logoutBtn = findViewById(R.id.logoutBtn)
        editProfile = findViewById(R.id.editProfile)
        changePassword = findViewById(R.id.changePassword)
        faq = findViewById(R.id.faq)
        contact = findViewById(R.id.contact)

        navHome = findViewById(R.id.navHome)
        navOrders = findViewById(R.id.navOrders)
        navSettings = findViewById(R.id.navSettings)
        profileImage = findViewById(R.id.profileImage)
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)

        // ✅ create(this) now returns ApiService directly (includes auth token)
        val api = RetrofitClient.create(this)

        presenter = SettingsPresenter(
            this,
            SettingsModel(api),
            this
        )

        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        presenter.loadProfile()

        editProfile.setOnClickListener {
            showUpdateProfileDialog()
        }

        changePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        faq.setOnClickListener {
            Toast.makeText(this, "FAQ clicked", Toast.LENGTH_SHORT).show()
        }

        contact.setOnClickListener {
            Toast.makeText(this, "Contact clicked", Toast.LENGTH_SHORT).show()
        }

        logoutBtn.setOnClickListener {
            presenter.logout()
        }
    }

    override fun showProfile(profile: ProfileResponse) {
        currentUserId = profile.id
        currentFirstName = profile.firstName
        currentLastName = profile.lastName

        profileName.text = "${profile.firstName} ${profile.lastName}"
        profileEmail.text = profile.email
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun updateProfileImage(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(profileImage)
    }


    private fun showUpdateProfileDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val fNameInput = EditText(this)
        fNameInput.hint = "First Name"
        layout.addView(fNameInput)

        val lNameInput = EditText(this)
        lNameInput.hint = "Last Name"
        layout.addView(lNameInput)

        AlertDialog.Builder(this)
            .setTitle("Update Profile")
            .setView(layout)
            .setPositiveButton("Update") { _, _ ->
                val fname = fNameInput.text.toString()
                val lname = lNameInput.text.toString()

                if (fname.isNotEmpty() && lname.isNotEmpty()) {
                    if (currentUserId != null) {
                        presenter.updateProfile(currentUserId!!, fname, lname)
                    } else {
                        Toast.makeText(this, "User not loaded yet", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showChangePasswordDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val curPass = EditText(this)
        curPass.hint = "Current Password"
        layout.addView(curPass)

        val newPass = EditText(this)
        newPass.hint = "New Password"
        layout.addView(newPass)

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(layout)
            .setPositiveButton("Change") { _, _ ->
                if (curPass.text.isNotEmpty() && newPass.text.isNotEmpty()) {
                    presenter.changePassword(
                        curPass.text.toString(),
                        newPass.text.toString()
                    )
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let {
                presenter.uploadAvatar(it)
            }
        }
    }
}