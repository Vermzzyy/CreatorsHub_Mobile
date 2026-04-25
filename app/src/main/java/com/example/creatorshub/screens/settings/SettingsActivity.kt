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
        val view = layoutInflater.inflate(R.layout.dialog_custom, null)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        
        val title = view.findViewById<TextView>(R.id.dialogTitle)
        val fNameInput = view.findViewById<EditText>(R.id.input1)
        val lNameInput = view.findViewById<EditText>(R.id.input2)
        val actionBtn = view.findViewById<Button>(R.id.dialogActionBtn)
        
        title.text = "Update Profile"
        fNameInput.hint = "First Name"
        fNameInput.setText(currentFirstName)
        lNameInput.hint = "Last Name"
        lNameInput.setText(currentLastName)
        actionBtn.text = "UPDATE"
        
        actionBtn.setOnClickListener {
            val fname = fNameInput.text.toString()
            val lname = lNameInput.text.toString()

            if (fname.isNotEmpty() && lname.isNotEmpty()) {
                if (currentUserId != null) {
                    presenter.updateProfile(currentUserId!!, fname, lname)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "User not loaded yet", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showChangePasswordDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_custom, null)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        
        val title = view.findViewById<TextView>(R.id.dialogTitle)
        val curPassInput = view.findViewById<EditText>(R.id.input1)
        val newPassInput = view.findViewById<EditText>(R.id.input2)
        val actionBtn = view.findViewById<Button>(R.id.dialogActionBtn)
        
        title.text = "Change Password"
        curPassInput.hint = "Current Password"
        curPassInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        newPassInput.hint = "New Password"
        newPassInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        actionBtn.text = "CHANGE"
        
        actionBtn.setOnClickListener {
            val curPass = curPassInput.text.toString()
            val newPass = newPassInput.text.toString()

            if (curPass.isNotEmpty() && newPass.isNotEmpty()) {
                presenter.changePassword(curPass, newPass)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
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