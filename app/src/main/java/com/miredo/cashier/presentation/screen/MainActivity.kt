package com.miredo.cashier.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.presentation.navigation.AuthAwareNavigation
import com.miredo.cashier.presentation.screen.auth.AuthViewModel
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.CashierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            CashierTheme {
                SetSystemBarsColorBasedOnAuth()
                AuthAwareNavigation()
            }
        }
    }
}

@Composable
private fun SetSystemBarsColorBasedOnAuth() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as ComponentActivity
            
            // Set status bar appearance based on authentication state
            when (authState) {
                is AuthState.Unauthenticated, 
                is AuthState.Loading, 
                is AuthState.Error -> {
                    // Login screen - use system default with light content
                    activity.enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            lightScrim = android.graphics.Color.TRANSPARENT,
                            darkScrim = android.graphics.Color.TRANSPARENT
                        )
                    )
                }
                is AuthState.Authenticated -> {
                    // Main app screens - use BlueTertiary with dark content  
                    activity.enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            scrim = BlueTertiary.toArgb()
                        )
                    )
                }
            }
        }
    }
}
