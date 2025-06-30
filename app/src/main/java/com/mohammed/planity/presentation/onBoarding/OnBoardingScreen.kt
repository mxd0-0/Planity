import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohammed.planity.ui.presentation.onBoarding.components.GradientButton
import com.mohammed.planity.ui.presentation.onBoarding.components.IllustrationSection
import com.mohammed.planity.ui.presentation.onBoarding.components.WelcomeText

// Define colors to match the design
val DarkBackground = Color(0xFF1A1A1A)
val PlanityPurple = Color(0xFF935DE6)
val PlanityPurpleLight = Color(0xFFB983FF)
val CardBackground = Color(0xFF2C2C2E)
val TextGray = Color.LightGray
val FlagRed = Color(0xFFE54B4B)
val FlagGreen = Color(0xFF30B858)

@Composable
fun OnboardingScreen(modifier: Modifier= Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IllustrationSection()
                WelcomeText()
            }
            GradientButton(
                text = "Get Start",
                modifier = modifier
                    .fillMaxWidth()

            )
        }
    }
}





@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun GetStartedScreenPreview() {
    OnboardingScreen(
        modifier = Modifier
        .safeContentPadding()
    )
}