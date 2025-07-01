package com.mohammed.planity.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohammed.planity.R
import com.mohammed.planity.ui.theme.errorContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenRoute(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onSignedOut: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSignedOut) {
        if (state.isSignedOut) {
            onSignedOut()
        }
    }

    SettingsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val showToast = { Toast.makeText(context, "Feature not implemented yet", Toast.LENGTH_SHORT).show() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { SettingsTopBar(onBackClicked = onNavigateBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Section
            ProfileHeader(name = state.userName ?: "User")
            Spacer(modifier = Modifier.height(32.dp))

            // Customize Section
            SettingsSection {
                SettingsRow(icon = R.drawable.edit, text = "Edit Your Name", onClick = showToast)
                SettingsRow(icon = R.drawable.outline_notifications_24, text = "Notification") {
                    Switch(checked = true, onCheckedChange = { showToast() })
                }
                SettingsRow(icon = R.drawable.resource_import, text = "Import Data", onClick = showToast)
                SettingsRow(icon = R.drawable.export, text = "Export data", onClick = showToast)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Out Button
            Button(
                onClick = { onEvent(SettingsEvent.OnSignOut) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = errorContainer)
            ) {
                Text("Sign Out", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Action Row
            FooterActions(onActionClick = showToast)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- UI Components for Settings Screen ---

@Composable
private fun SettingsTopBar(onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text("Settings", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
private fun ProfileHeader(name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile), // Placeholder avatar
            contentDescription = "Avatar",
            modifier = Modifier.size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsSection( content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Customize", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
private fun SettingsRow(icon: Int, text: String, onClick: (() -> Unit)? = null, trailingContent: (@Composable () -> Unit)? = null) {
    val rowModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Row(
        modifier = rowModifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = text, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f))
        if (trailingContent != null) {
            trailingContent()
        } else if (onClick != null) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FooterActions(onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        FooterActionItem(icon = R.drawable.star, label = "Rate us", onClick = onActionClick)
        FooterActionItem(icon = R.drawable.category_selected, label = "Policy", onClick = onActionClick)
        FooterActionItem(icon = R.drawable.share, label = "Share", onClick = onActionClick)
        FooterActionItem(icon = R.drawable.more, label = "More...", onClick = onActionClick)
    }
}

@Composable
private fun FooterActionItem(icon: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = label, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}