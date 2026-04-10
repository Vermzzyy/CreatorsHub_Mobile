package com.example.creatorshub.screens.register.model

/**
 * Supabase Auth signup response.
 * On success, Supabase returns the new user's id and email.
 * On error (e.g. email already used), it returns a non-2xx status.
 */
data class RegisterResponse(
    val id: String? = null,
    val email: String? = null,
    val message: String? = null
)