package com.mohammed.planity.ui.presentation.onBoarding

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.mohammed.planity.ui.presentation.onBoarding.components.NameTextField
import com.mohammed.planity.ui.theme.PlanityTheme

// Re-using colors from the previous screen for consistency
val DarkBackground = Color(0xFF121212) // A slightly darker background for this screen
val PlanityPurple = Color(0xFF935DE6)
val TextColorGray = Color(0xFF8A8A8E)
val TextFieldPlaceholderColor = Color(0xFF6C6C6E)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EnterNameScreen(
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content area that pushes the button to the bottom
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Align content to the top of its space
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

                // Spacer to push the text field down towards the center
                Spacer(modifier = Modifier.weight(0.5f))

                NameTextField(
                    value = name,
                    onValueChange = { name = it }
                )

                // Spacer to fill remaining space
                Spacer(modifier = Modifier.weight(1f))
            }

            // Button at the bottom
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
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
        EnterNameScreen()
    }
}