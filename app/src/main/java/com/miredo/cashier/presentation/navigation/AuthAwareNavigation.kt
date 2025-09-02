package com.miredo.cashier.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miredo.cashier.data.model.Screen
import com.miredo.cashier.domain.model.AuthState
import com.miredo.cashier.navigation.NavigationArgs
import com.miredo.cashier.presentation.screen.auth.AuthViewModel
import com.miredo.cashier.presentation.screen.auth.LoginScreen
import com.miredo.cashier.presentation.screen.checkin.CheckInScreen
import com.miredo.cashier.presentation.screen.checkout.CheckoutScreen
import com.miredo.cashier.presentation.screen.counter.CounterScreen
import com.miredo.cashier.presentation.screen.home.HomeScreen
import com.miredo.cashier.presentation.screen.sale.SaleScreen

@Composable
fun AuthAwareNavigation() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()
    val navController = rememberNavController()

    val startDestination = when (authState) {
        is AuthState.Authenticated -> Screen.Home.route
        is AuthState.Unauthenticated -> Screen.Login.route
        is AuthState.Loading -> Screen.Login.route
        is AuthState.Error -> Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Screens (Protected)
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCheckIn = {
                    navController.navigate(Screen.CheckIn.route)
                },
                onNavigateToSale = { reportId ->
                    navController.navigate(Screen.Sale.createRoute(reportId))
                },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CheckIn.route) {
            CheckInScreen(navController = navController)
        }

        composable(Screen.Sale.route) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString(NavigationArgs.REPORT_ID).orEmpty()
            SaleScreen(
                reportId = reportId,
                navController = navController
            )
        }

        composable(Screen.Counter.route) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString(NavigationArgs.REPORT_ID).orEmpty()
            CounterScreen(
                reportId = reportId,
                navController = navController
            )
        }

        composable(Screen.CounterEdit.route) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString(NavigationArgs.REPORT_ID).orEmpty()
            val saleId = backStackEntry.arguments?.getString(NavigationArgs.SALE_ID).orEmpty()
            CounterScreen(
                reportId = reportId,
                saleId = saleId,
                navController = navController
            )
        }

        composable(Screen.CheckOut.route) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString(NavigationArgs.REPORT_ID).orEmpty()
            CheckoutScreen(
                reportId = reportId,
                navController = navController
            )
        }
    }
}