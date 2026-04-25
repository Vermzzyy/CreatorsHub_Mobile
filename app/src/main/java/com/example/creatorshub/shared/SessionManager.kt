package com.example.creatorshub.shared

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("TOKEN", null)
    }

    fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("USER_ID", null)
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}