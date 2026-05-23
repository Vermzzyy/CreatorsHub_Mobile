package com.example.creatorshub.data

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("APP", Context.MODE_PRIVATE)

    // ── Supabase token (used for auth/v1, rest/v1, storage/v1) ──────────────
    fun saveToken(token: String) {
        prefs.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("TOKEN", null)
    }

    // ── Supabase user ID ─────────────────────────────────────────────────────
    fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("USER_ID", null)
    }

    // ── Spring Boot backend JWT (used for /api/v1/services, /api/v1/orders) ─
    fun saveBackendToken(token: String) {
        prefs.edit().putString("BACKEND_TOKEN", token).apply()
    }

    fun getBackendToken(): String? {
        return prefs.getString("BACKEND_TOKEN", null)
    }

    // ── Clear everything on logout ───────────────────────────────────────────
    fun clearToken() {
        prefs.edit().clear().apply()
    }
}