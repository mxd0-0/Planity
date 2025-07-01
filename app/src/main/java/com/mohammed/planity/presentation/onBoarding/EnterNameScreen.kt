package com.mohammed.planity.presentation.onBoarding

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.presentation.onBoarding.components.NameTextField
import com.mohammed.planity.ui.theme.PlanityTheme

// Re-using colors from the previous screen for consistency
val DarkBackground = Color(0xFF121212) // A slightly darker background for this screen
val PlanityPurple = Color(0xFF935DE6)
val TextColorGray = Color(0xFF8A8A8E)
val TextFieldPlaceholderColor = Color(0xFF6C6C6E)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EnterNameScreen(
    onNameEntered: (String) -> Unit, // <-- ADD THIS PARAMETER
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }

    // Use Surface instead of Scaffold for a simpler layout
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Use theme color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "What is Your Name ?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Please provide your name, which we'll use for display and personalize your to-do experience.",
                    color = TextColorGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.weight(0.5f))
                NameTextField(
                    value = name,
                    onValueChange = { name = it }
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                // Use the passed-in lambda, sending the trimmed name
                onClick = { onNameEntered(name.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // Disable the button if the name is empty or just whitespace
                enabled = name.isNotBlank(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    // Use a disabled color to give user feedback
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun EnterNameScreenPreview() {
    PlanityTheme {
        EnterNameScreen(onNameEntered = {})
    }
}
