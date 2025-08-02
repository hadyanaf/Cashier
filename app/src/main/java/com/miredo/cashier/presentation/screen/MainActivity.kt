package com.miredo.cashier.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miredo.cashier.data.model.Screen
import com.miredo.cashier.presentation.screen.checkin.CheckInScreen
import com.miredo.cashier.presentation.screen.home.HomeScreen
import com.miredo.cashier.presentation.ui.theme.CashierTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashierTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCheckIn = {
                    navController.navigate(Screen.CheckIn.route)
                }
            )
        }

        composable(Screen.CheckIn.route) {
            CheckInScreen(navController = navController)
        }
    }
}