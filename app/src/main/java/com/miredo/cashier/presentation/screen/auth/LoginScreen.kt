package com.miredo.cashier.presentation.screen.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.presentation.components.CustomButton
import com.miredo.cashier.presentation.components.CustomTextField
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.BlueSecondary
import com.miredo.cashier.presentation.ui.theme.CashierTheme
import com.miredo.cashier.presentation.ui.theme.GrayText
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isSignUp by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onNavigateToHome()
            }
            else -> {}
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    LoginScreenContent(
        modifier = modifier,
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = { confirmPassword = it },
        rememberMe = rememberMe,
        onRememberMeChange = { rememberMe = it },
        isSignUp = isSignUp,
        onToggleSignUp = { isSignUp = it },
        isLoading = isLoading,
        onLoginClick = { viewModel.signIn(email, password) },
        onSignUpClick = { viewModel.signUp(email, password, confirmPassword) },
        onForgotPasswordClick = { viewModel.sendPasswordResetEmail(email) },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    isSignUp: Boolean,
    onToggleSignUp: (Boolean) -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        GradientBackground {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo and App Name
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "App Logo",
                        modifier = Modifier.size(80.dp),
                        tint = BluePrimary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Kasir",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )
                    
                    Text(
                        text = "Kelola bisnis Anda dengan mudah",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Login Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = if (isSignUp) "Buat Akun" else "Selamat Datang",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = BluePrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text = if (isSignUp) "Daftar untuk memulai" else "Masuk ke akun Anda",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrayText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Email Field
                            CustomTextField(
                                value = email,
                                onValueChange = onEmailChange,
                                label = "Alamat Email",
                                keyboardType = KeyboardType.Email
                            )

                            // Password Field
                            CustomTextField(
                                value = password,
                                onValueChange = onPasswordChange,
                                label = "Kata Sandi",
                                isPassword = true
                            )

                            // Confirm Password Field (for Sign Up)
                            if (isSignUp) {
                                CustomTextField(
                                    value = confirmPassword,
                                    onValueChange = onConfirmPasswordChange,
                                    label = "Konfirmasi Kata Sandi",
                                    isPassword = true
                                )
                            }

                            if (!isSignUp) {
                                // Remember Me and Forgot Password
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = rememberMe,
                                            onCheckedChange = onRememberMeChange,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = BluePrimary,
                                                uncheckedColor = GrayText
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Ingat saya",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GrayText
                                        )
                                    }

                                    Text(
                                        text = "Lupa Kata Sandi?",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BluePrimary,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable { onForgotPasswordClick() }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Action Button
                            CustomButton(
                                text = if (isSignUp) "Buat Akun" else "Masuk",
                                onClick = if (isSignUp) onSignUpClick else onLoginClick,
                                isLoading = isLoading
                            )

                            // Toggle Sign Up/Login
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isSignUp) "Sudah punya akun? " else "Belum punya akun? ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrayText
                                )
                                TextButton(
                                    onClick = { onToggleSignUp(!isSignUp) }
                                ) {
                                    Text(
                                        text = if (isSignUp) "Masuk" else "Daftar",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BluePrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    CashierTheme {
        LoginScreenContent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            rememberMe = false,
            onRememberMeChange = {},
            isSignUp = false,
            onToggleSignUp = {},
            isLoading = false,
            onLoginClick = {},
            onSignUpClick = {},
            onForgotPasswordClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}