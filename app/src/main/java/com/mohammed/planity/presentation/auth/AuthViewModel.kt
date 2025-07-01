package com.mohammed.planity.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignIn -> signIn(event.email, event.password)
            is AuthEvent.OnSignUp -> signUp(event.name, event.email, event.password)
        }
    }

    private fun signUp(name: String, email: String, pass: String) {
        if (!isValid(email, pass, name)) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // 1. Create the user with email and password
                val result = auth.createUserWithEmailAndPassword(email.trim(), pass.trim()).await()

                // 2. Create a request to update the user's profile
                val profileUpdates = userProfileChangeRequest {
                    // --- THIS IS THE CORRECT METHOD ---
                    displayName = name.trim()
                }

                // 3. Apply the update to the newly created user
                result.user?.updateProfile(profileUpdates)?.await()

                // 4. Update state to signal success
                _uiState.update { it.copy(isLoading = false, isAuthSuccessful = true) }

            } catch (e: FirebaseAuthUserCollisionException) {
                _uiState.update { it.copy(isLoading = false, error = "This email is already in use.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Sign up failed.") }
            }
        }
    }
    private fun signIn(email: String, pass: String) {
        if (!isValid(email, pass)) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                auth.signInWithEmailAndPassword(email.trim(), pass.trim()).await()
                _uiState.update { it.copy(isLoading = false, isAuthSuccessful = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Login failed. Check credentials.") }
            }
        }
    }

    private fun isValid(email: String, pass: String, name: String? = null): Boolean {
        if (email.isBlank() || pass.isBlank() || (name != null && name.isBlank())) {
            _uiState.update { it.copy(error = "All fields are required.") }
            return false
        }
        if (pass.length < 6) {
            _uiState.update { it.copy(error = "Password must be at least 6 characters.") }
            return false
        }
        _uiState.update { it.copy(error = null) }
        return true
    }
}