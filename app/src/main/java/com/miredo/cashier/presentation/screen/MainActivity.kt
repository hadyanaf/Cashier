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
import com.miredo.cashier.navigation.NavigationArgs
import com.miredo.cashier.presentation.screen.checkin.CheckInScreen
import com.miredo.cashier.presentation.screen.checkout.CheckoutScreen
import com.miredo.cashier.presentation.screen.counter.CounterScreen
import com.miredo.cashier.presentation.screen.home.HomeScreen
import com.miredo.cashier.presentation.screen.sale.SaleScreen
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
                },
                onNavigateToSale = { reportId ->
                    navController.navigate(Screen.Sale.createRoute(reportId))
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