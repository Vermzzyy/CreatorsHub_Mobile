package com.example.creatorshub.screens.settings


import com.example.creatorshub.models.ProfileResponse

interface SettingsContract {

    interface View {
        fun showProfile(profile: ProfileResponse)
        fun showError(message: String)
        fun showMessage(message: String)
        fun navigateToLogin()
        fun updateProfileImage(url: String)
    }

    interface Presenter {
        fun loadProfile()
        fun logout()
        fun updateProfile(userId: String, fName: String, lName: String)
        fun changePassword(curPass: String, newPass: String)
        fun uploadAvatar(uri: android.net.Uri)
    }
}