package com.mohammed.planity.presentation.onBoarding.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohammed.planity.R
import com.mohammed.planity.ui.theme.errorContainer
import com.mohammed.planity.ui.theme.flagGreen

@Composable
fun IllustrationSection() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ilustartion), // Replace with your SVG
            contentDescription = "Illustration of a person planning",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .offset(y= (-100).dp)
                .align(Alignment.BottomCenter)
            ,
        ) {
            TaskCard(
                title = "Design workshop",
                date = "7,june,2023",
                flagColor = errorContainer,
            )
            TaskCard(
                title = "Create New Post",
                date = "7,june,2023",
                flagColor = flagGreen,
                rotation = -10f
            )
        }
    }
}

@Preview
@Composable
private fun IllustartionPrev() {
    IllustrationSection()

}