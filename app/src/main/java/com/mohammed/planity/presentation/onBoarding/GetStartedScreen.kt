package com.mohammed.planity.presentation.onBoarding


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.presentation.onBoarding.components.IllustrationSection
import com.mohammed.planity.ui.theme.PlanityTheme

// --- Reusable Color Definitions ---
private val PlanityPurpleLight = Color(0xFFB983FF)

@Composable
fun GetStartedScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IllustrationSection()
                WelcomeText()
            }
            // The main action button
            GradientButton(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = onGetStartedClick // Calls the lambda from the NavHost
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- Helper UI Components ---





@Composable
private fun WelcomeText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("Welcome To ")
                withStyle(style = SpanStyle(color = PlanityPurple, fontWeight = FontWeight.Bold)) {
                    append("Planity")
                }
            },
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Plan + Simplicity",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val gradient = Brush.horizontalGradient(listOf(PlanityPurpleLight, PlanityPurple))
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Get Start", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun GetStartedScreenPreview() {
    PlanityTheme {
        GetStartedScreen(onGetStartedClick = {})
    }
}