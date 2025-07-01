package com.mohammed.planity.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mohammed.planity.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class StartDestination {
    ONBOARDING,
    AUTH,
    MAIN_APP
}

class RootViewModel(
    private val sessionManager: SessionManager,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkInitialDestination()
    }

    private fun checkInitialDestination() {
        viewModelScope.launch {
            // Check if the user is already logged in with Firebase
            if (auth.currentUser != null) {
                // If logged in, go straight to the main app
                _startDestination.value = StartDestination.MAIN_APP
            } else {
                // If not logged in, check if they've completed the initial onboarding screens
                sessionManager.onboardingCompleteFlow.collect { hasCompleted ->
                    if (hasCompleted) {
                        // If they have, go to the Auth screen
                        _startDestination.value = StartDestination.AUTH
                    } else {
                        // If they haven't, start with Onboarding
                        _startDestination.value = StartDestination.ONBOARDING
                    }
                }
            }
        }
    }

    // This will be called after the user finishes the "Enter Name" screen
    fun onOnboardingFinished() {
        viewModelScope.launch {
            sessionManager.setOnboardingComplete()
            // The state will automatically update to AUTH because the flow will emit 'true'
        }
    }
}