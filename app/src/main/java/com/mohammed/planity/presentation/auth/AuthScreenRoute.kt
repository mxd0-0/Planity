package com.mohammed.planity.ui.presentation.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mohammed.planity.presentation.auth.AuthEvent
import com.mohammed.planity.presentation.auth.AuthState
import com.mohammed.planity.presentation.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

enum class AuthMode {
    LOGIN, SIGN_UP
}

@Composable
fun AuthScreenRoute(
    viewModel: AuthViewModel = koinViewModel(),
    onAuthSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthSuccessful) {
        if (uiState.isAuthSuccessful) {
            onAuthSuccess()
        }
    }

    AuthScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AuthScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Crossfade provides a smooth animation when switching between Login and Sign Up
            Crossfade(targetState = authMode, label = "AuthModeAnimation") { mode ->
                when (mode) {
                    AuthMode.LOGIN -> LoginContent(
                        state = state,
                        onLogin = { email, password -> onEvent(AuthEvent.OnSignIn(email, password)) },
                        onSwitchToSignUp = { authMode = AuthMode.SIGN_UP }
                    )
                    AuthMode.SIGN_UP -> SignUpContent(
                        state = state,
                        onSignUp = { name, email, password -> onEvent(AuthEvent.OnSignUp(name, email, password)) },
                        onSwitchToLogin = { authMode = AuthMode.LOGIN }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginContent(
    state: AuthState,
    onLogin: (String, String) -> Unit,
    onSwitchToSignUp: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("Log In", style = MaterialTheme.typography.headlineLarge)
        AuthSwitchText("Don't have an account?", " Sign Up", onSwitchToSignUp)
        Spacer(modifier = Modifier.height(32.dp))

        // Using "Full Name" field for email as per design, but can be changed
        AuthTextField(label = "Email", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(value = password, onValueChange = { password = it })

        if (state.error != null) {
            AuthErrorText(state.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
        AuthButton("Log In", isLoading = state.isLoading) {
            onLogin(email, password)
        }
        TextButton(onClick = { /* TODO: Forgot Password */ }) {
            Text("Forgot Password?")
        }
    }
}

@Composable
private fun SignUpContent(
    state: AuthState,
    onSignUp: (String, String, String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Sign Up", style = MaterialTheme.typography.headlineLarge)
        AuthSwitchText("Have an account?", " Log In", onSwitchToLogin)
        Spacer(modifier = Modifier.height(32.dp))

        AuthTextField(label = "Full Name", value = name, onValueChange = { name = it })
        Spacer(modifier = Modifier.height(16.dp))
        AuthTextField(label = "Email", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(value = password, onValueChange = { password = it })

        if (state.error != null) {
            AuthErrorText(state.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
        AuthButton("Sign Up", isLoading = state.isLoading) {
            onSignUp(name, email, password)
        }
        TextButton(onClick = { /* TODO: Forgot Password */ }) {
            Text("Forgot Password?")
        }
    }
}


// --- Helper UI Components ---

@Composable
private fun AuthTextField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun PasswordTextField(value: String, onValueChange: (String) -> Unit) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Face
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun AuthButton(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        enabled = !isLoading,
        shape = RoundedCornerShape(16.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
        } else {
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun AuthSwitchText(staticText: String, clickableText: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        append(staticText)
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        ) {
            pushStringAnnotation(tag = "SWITCH", annotation = "switch")
            append(clickableText)
            pop()
        }
    }
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SWITCH", start = offset, end = offset)
                .firstOrNull()?.let {
                    onClick()
                }
        }
    )
}

@Composable
private fun AuthErrorText(error: String) {
    Text(
        text = error,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp)
    )
}