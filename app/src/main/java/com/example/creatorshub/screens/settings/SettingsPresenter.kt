package com.example.creatorshub.screens.settings

import android.content.Context
import android.net.Uri
import com.example.creatorshub.shared.SessionManager
import com.example.creatorshub.models.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.*

class SettingsPresenter(
    private val view: SettingsContract.View,
    private val model: SettingsModel,
    private val context: Context
) : SettingsContract.Presenter {

    private var currentUserId: String? = null
    private var firstName: String = ""
    private var lastName: String = ""

    override fun loadProfile() {
        // Load user ID from session (saved at login)
        currentUserId = SessionManager(context).getUserId()

        model.getProfile().enqueue(object : Callback<List<ProfileResponse>> {
            override fun onResponse(call: Call<List<ProfileResponse>>, response: Response<List<ProfileResponse>>) {
                if (response.isSuccessful) {
                    val profile = response.body()?.firstOrNull()
                    if (profile != null) {
                        currentUserId = profile.id ?: currentUserId
                        firstName = profile.firstName
                        lastName = profile.lastName
                        view.showProfile(profile)
                    } else {
                        view.showError("Profile not found")
                    }
                } else if (response.code() == 401) {
                    logout()
                } else {
                    view.showError("Failed to load profile (${response.code()})")
                }
            }

            override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                view.showError("Network error")
            }
        })
    }

    override fun updateProfile(userId: String, fName: String, lName: String) {
        val request = UpdateProfileRequest(fName, lName)

        model.updateProfile(userId, request)
            .enqueue(object : Callback<List<ProfileResponse>> {
                override fun onResponse(call: Call<List<ProfileResponse>>, response: Response<List<ProfileResponse>>) {
                    if (response.isSuccessful) {
                        firstName = fName
                        lastName = lName
                        view.showMessage("Profile updated")
                    } else {
                        view.showError("Update failed (${response.code()})")
                    }
                }

                override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                    view.showError("Network error")
                }
            })
    }

    override fun changePassword(curPass: String, newPass: String) {
        // Note: Supabase Auth does not validate the old password server-side.
        // The request only sends the new password.
        val request = ChangePasswordRequest(newPass)

        model.changePassword(request)
            .enqueue(object : Callback<GenericResponse> {
                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                    if (response.isSuccessful) {
                        view.showMessage("Password changed successfully")
                    } else {
                        view.showError("Change failed (${response.code()})")
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    view.showError("Network error")
                }
            })
    }

    override fun uploadAvatar(uri: Uri) {

        val userId = currentUserId ?: run {
            view.showError("User not loaded yet")
            return
        }

        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        if (bytes == null) {
            view.showError("Failed to read image")
            return
        }

        val filename = "avatar_${userId}_${System.currentTimeMillis()}.jpg"

        val requestBody = RequestBody.create(
            MediaType.parse("image/jpeg"),
            bytes
        )

        model.uploadAvatar(filename, requestBody)
            .enqueue(object : Callback<GenericResponse> {

                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {

                    if (response.isSuccessful) {

                        val publicUrl =
                            "https://wilhjpvrfrscquauuvle.supabase.co/storage/v1/object/public/avatars/$filename"

                        val updateRequest = UpdateProfileRequest(
                            firstName,
                            lastName,
                            publicUrl
                        )

                        model.updateProfile(userId, updateRequest)
                            .enqueue(object : Callback<List<ProfileResponse>> {
                                override fun onResponse(call: Call<List<ProfileResponse>>, response: Response<List<ProfileResponse>>) {
                                    if (response.isSuccessful) {
                                        view.updateProfileImage(publicUrl)
                                        view.showMessage("Profile picture updated!")
                                    } else {
                                        view.showError("Failed to save avatar URL (${response.code()})")
                                    }
                                }

                                override fun onFailure(call: Call<List<ProfileResponse>>, t: Throwable) {
                                    view.showError("Network error")
                                }
                            })

                    } else {
                        view.showError("Upload failed (${response.code()})")
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    view.showError("Upload network error")
                }
            })
    }

    override fun logout() {
        val session = SessionManager(context)
        session.clearToken()
        view.navigateToLogin()
    }
}