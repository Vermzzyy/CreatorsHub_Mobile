package com.example.creatorshub.screens.register.model

/**
 * Supabase Auth signup request.
 * First name and last name go inside the "data" object
 * so that Supabase stores them in user_metadata.
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val data: UserData
) {
    data class UserData(
        val first_name: String,
        val last_name: String
    )
}