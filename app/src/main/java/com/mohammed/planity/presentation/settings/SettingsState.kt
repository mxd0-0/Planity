package com.mohammed.planity.presentation.settings

data class SettingsState(
    val userName: String? = null,
    val userEmail: String? = null,
    val isSignedOut: Boolean = false
)

sealed class SettingsEvent {
    object OnSignOut : SettingsEvent()
    // Add other events for name changes, etc. later
}