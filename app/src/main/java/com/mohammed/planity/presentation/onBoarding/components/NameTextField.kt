package com.mohammed.planity.ui.presentation.onBoarding.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.ui.presentation.onBoarding.PlanityPurple
import com.mohammed.planity.ui.presentation.onBoarding.TextFieldPlaceholderColor
import com.mohammed.planity.ui.theme.gray400
import com.mohammed.planity.ui.theme.gray600

@Composable
fun NameTextField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        placeholder = {
            Text(
                text = "Enter Your Name",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = TextFieldPlaceholderColor,
                fontSize = 20.sp
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = gray400,
            unfocusedIndicatorColor = gray600,
            cursorColor = PlanityPurple
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        ),
        singleLine = true
    )
}