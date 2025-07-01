package com.mohammed.planity.presentation.settings

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mohammed.planity.domain.use_case.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.OnSignOut -> {
                signOutUseCase()
                _state.update { it.copy(isSignedOut = true) }
            }
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        _state.update {
            it.copy(
                userName = currentUser?.displayName,
                userEmail = currentUser?.email
            )
        }
    }
}