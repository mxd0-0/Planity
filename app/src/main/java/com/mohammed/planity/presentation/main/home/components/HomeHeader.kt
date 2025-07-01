package com.mohammed.planity.presentation.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeHeader(userName: String, onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Surface(
                modifier = Modifier
                    .offset(x = 72.dp, y = (-4).dp)
                    .rotate(-15f),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "to-do",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun Home() {
    HomeHeader("Planity",{})
}