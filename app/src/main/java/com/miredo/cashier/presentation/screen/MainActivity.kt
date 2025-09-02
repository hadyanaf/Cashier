package com.miredo.cashier.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.miredo.cashier.presentation.navigation.AuthAwareNavigation
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.CashierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashierTheme {
                SetSystemBarsColor()
                AuthAwareNavigation()
            }
        }
    }
}

@Composable
private fun SetSystemBarsColor() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as ComponentActivity).window
            window.statusBarColor = BlueTertiary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
}
