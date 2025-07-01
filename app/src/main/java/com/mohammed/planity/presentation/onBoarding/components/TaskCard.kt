package com.mohammed.planity.presentation.onBoarding.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.R
import com.mohammed.planity.ui.theme.PlanityPurple
import com.mohammed.planity.ui.theme.TextGray
import com.mohammed.planity.ui.theme.cardBackgroundColor

@Composable
fun TaskCard(title: String,
             date: String,
             flagColor: Color,
             rotation: Float= 0f) {
    Surface(
        color = cardBackgroundColor,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(90.dp)
            .rotate(rotation)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PlanityPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.flag),
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = date, color = TextGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.flag),
                contentDescription = "Priority Flag",
                tint = flagColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}