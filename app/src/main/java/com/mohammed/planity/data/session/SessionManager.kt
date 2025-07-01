package com.mohammed.planity.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
// Create a DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "planity_session")

class SessionManager(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETE_KEY = booleanPreferencesKey("onboarding_complete")
    }

    // Flow to observe if onboarding is complete
    val onboardingCompleteFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETE_KEY] ?: false
        }

    // Function to set the flag to true
    suspend fun setOnboardingComplete() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE_KEY] = true
        }
    }
}