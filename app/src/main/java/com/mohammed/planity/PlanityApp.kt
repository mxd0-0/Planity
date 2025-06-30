package com.mohammed.planity

import android.app.Application
import com.mohammed.planity.di.appModule // We will create this file next
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PlanityApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // --- START KOIN ---
        startKoin {
            // Log Koin activity
            androidLogger()
            // Provide the Android context to Koin
            androidContext(this@PlanityApp)
            // Load our modules
            modules(appModule)
        }
    }
}