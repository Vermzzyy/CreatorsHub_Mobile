package com.example.testapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.HomeActivity
import com.example.testapplication.LoginActivity
import com.example.testapplication.R
import com.example.testapplication.api.RetrofitClient
import com.example.testapplication.models.ChangePasswordRequest
import com.example.testapplication.models.GenericResponse
import com.example.testapplication.models.ProfileResponse
import com.example.testapplication.models.UpdateProfileRequest
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : Activity() {

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

    private fun getToken(): String? {
        val pref = getSharedPreferences("APP", MODE_PRIVATE)
        return pref.getString("TOKEN", null)
    }

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

        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        navSettings.setOnClickListener {
            // we are already in settings
        }
        
        loadProfile()

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
            Toast.makeText(this, "Contact Us clicked", Toast.LENGTH_SHORT).show()
        }

        logoutBtn.setOnClickListener {
            val pref = getSharedPreferences("APP", MODE_PRIVATE)
            pref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile() {
        val token = getToken() ?: return

        RetrofitClient.instance.profile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body() ?: return
                    currentUserId = profile.id
                    currentFirstName = profile.firstName
                    currentLastName = profile.lastName
                    
                    profileName.text = "${profile.firstName} ${profile.lastName}"
                    profileEmail.text = profile.email
                    
                    if (!profile.avatar_url.isNullOrEmpty()) {
                        Glide.with(this@SettingsActivity)
                            .load(profile.avatar_url)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profileImage)
                    }

                    Toast.makeText(this@SettingsActivity, "Profile Loaded", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 401) {
                    logout()
                } else {
                    Toast.makeText(this@SettingsActivity, "Failed to load profile: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@SettingsActivity, "Network error loading profile", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun logout() {
        val pref = getSharedPreferences("APP", MODE_PRIVATE)
        pref.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
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
                    updateProfile(fname, lname)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProfile(fName: String, lName: String) {
        val token = getToken() ?: return
        val userId = currentUserId ?: run {
            Toast.makeText(this, "Profile ID not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }
        
        val request = UpdateProfileRequest(fName, lName)
        
        RetrofitClient.instance.updateProfile("eq.$userId", "Bearer $token", request).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SettingsActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@SettingsActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showChangePasswordDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val curPassInput = EditText(this)
        curPassInput.hint = "Current Password"
        curPassInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(curPassInput)

        val newPassInput = EditText(this)
        newPassInput.hint = "New Password"
        newPassInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(newPassInput)

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(layout)
            .setPositiveButton("Change") { _, _ ->
                val curPass = curPassInput.text.toString()
                val newPass = newPassInput.text.toString()
                if (curPass.isNotEmpty() && newPass.isNotEmpty()) {
                    changePassword(curPass, newPass)
                } else {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun changePassword(curPass: String, newPass: String) {
        val token = getToken() ?: return
        val request = ChangePasswordRequest(curPass, newPass)
        
        RetrofitClient.instance.changePassword("Bearer $token", request).enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SettingsActivity, "Password changed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SettingsActivity, "Change password failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Toast.makeText(this@SettingsActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uploadProfilePicture(data.data!!)
        }
    }

    private fun uploadProfilePicture(uri: android.net.Uri) {
        val token = getToken() ?: return
        val userId = currentUserId ?: return

        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()

        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        if (bytes == null) {
            Toast.makeText(this, "Failed to read image", Toast.LENGTH_SHORT).show()
            return
        }

        val filename = "avatar_${System.currentTimeMillis()}.jpg"
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes)

        RetrofitClient.instance.uploadAvatar(
            filename,
            "Bearer $token",
            "image/jpeg",
            requestBody
        ).enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    val publicUrl = "https://fmkdrxpdiroqsfblyavt.supabase.co/storage/v1/object/public/avatars/$filename"
                    
                    // Now update the profiles table
                    val updateRequest = UpdateProfileRequest(currentFirstName, currentLastName, publicUrl)
                    RetrofitClient.instance.updateProfile("eq.$userId", "Bearer $token", updateRequest).enqueue(object : Callback<ProfileResponse> {
                        override fun onResponse(call: Call<ProfileResponse>, pResp: Response<ProfileResponse>) {
                            if (pResp.isSuccessful) {
                                Glide.with(this@SettingsActivity)
                                    .load(publicUrl)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(profileImage)
                                Toast.makeText(this@SettingsActivity, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@SettingsActivity, "Failed to update profile DB", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            Toast.makeText(this@SettingsActivity, "Network Error saving picture URL", Toast.LENGTH_SHORT).show()
                        }
                    })

                } else {
                    Toast.makeText(this@SettingsActivity, "Upload failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Toast.makeText(this@SettingsActivity, "Network Error uploading image", Toast.LENGTH_SHORT).show()
            }
        })
    }
}