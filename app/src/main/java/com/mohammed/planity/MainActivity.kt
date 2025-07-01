package com.mohammed.planity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mohammed.planity.presentation.navigation.RootNavHost
import com.mohammed.planity.ui.theme.PlanityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanityTheme {
                RootNavHost()
            }
        }
    }
}