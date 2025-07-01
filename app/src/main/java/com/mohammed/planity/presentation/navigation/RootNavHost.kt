package com.mohammed.planity.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohammed.planity.R
import com.mohammed.planity.presentation.MainScreen
import com.mohammed.planity.ui.presentation.auth.AuthScreenRoute
import com.mohammed.planity.ui.theme.DarkBackground
import org.koin.androidx.compose.koinViewModel


@Composable
fun RootNavHost(
    viewModel: RootViewModel = koinViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()

    // This NavController will manage the top-level transitions between major app states
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = "initial_check") {
        composable("initial_check") {
            Box(
                modifier = Modifier
                    .background(DarkBackground)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ){
                Image(painter = painterResource(R.drawable.ic_launcher_foreground),null)
            }
        }

        composable(OnboardingDestinations.GET_STARTED,  exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
        ) {
            // The onboarding flow has its own internal navigation
            val onboardingNavController = rememberNavController()
            OnboardingNavHost(
                navController = onboardingNavController,
                onOnboardingFinished = {
                    viewModel.onOnboardingFinished()
                    // After finishing, navigate to the auth flow and clear the onboarding backstack
                    rootNavController.navigate(OnboardingDestinations.AUTH_ROUTE) {
                        popUpTo(OnboardingDestinations.GET_STARTED) { inclusive = true }
                    }
                }
            )
        }

        composable(OnboardingDestinations.AUTH_ROUTE) {
            AuthScreenRoute(
                onAuthSuccess = {
                    // After successful login, go to the main app and clear the auth backstack
                    rootNavController.navigate(Destinations.Home.route) {
                        popUpTo(OnboardingDestinations.AUTH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.Home.route) {
            MainScreen(
                onSignOut = {
                    // When user signs out from the main app, go back to the auth flow
                    rootNavController.navigate(OnboardingDestinations.AUTH_ROUTE) {
                        popUpTo(Destinations.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }

    // This effect observes the initial state from the ViewModel and performs the first navigation
    LaunchedEffect(startDestination) {
        when (startDestination) {
            StartDestination.MAIN_APP -> rootNavController.navigate(Destinations.Home.route) {
                popUpTo("initial_check") { inclusive = true }
            }
            StartDestination.AUTH -> rootNavController.navigate(OnboardingDestinations.AUTH_ROUTE) {
                popUpTo("initial_check") { inclusive = true }
            }
            StartDestination.ONBOARDING -> rootNavController.navigate(OnboardingDestinations.GET_STARTED) {
                popUpTo("initial_check") { inclusive = true }
            }
            null -> { /* Do nothing, wait for the state to be determined */ }
        }
    }
}