package com.mohammed.planity.presentation.auth

sealed class AuthEvent {
    // We now pass the name for sign-up
    data class OnSignUp(val name: String, val email: String, val password: String) : AuthEvent()
    data class OnSignIn(val email: String, val password: String) : AuthEvent()
}