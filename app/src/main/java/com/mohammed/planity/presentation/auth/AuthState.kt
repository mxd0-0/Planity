package com.mohammed.planity.presentation.auth


// This data class represents the entire state of the Auth screen.
// It's immutable, making it safe to use with Compose.
data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthSuccessful: Boolean = false
)