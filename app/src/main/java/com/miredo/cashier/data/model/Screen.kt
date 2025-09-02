package com.miredo.cashier.data.model

import com.miredo.cashier.navigation.NavigationArgs

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CheckIn: Screen("check_in")
    object CheckOut: Screen("checkout/{${NavigationArgs.REPORT_ID}}") {
        fun createRoute(reportId: String) = "checkout/$reportId"
    }
    object Sale: Screen("sale/{${NavigationArgs.REPORT_ID}}") {
        fun createRoute(reportId: String) = "sale/$reportId"
    }
    object Counter : Screen("counter/{${NavigationArgs.REPORT_ID}}") {
        fun createRoute(reportId: String) = "counter/$reportId"
    }
    object CounterEdit : Screen("counter/{${NavigationArgs.REPORT_ID}}/{${NavigationArgs.SALE_ID}}") {
        fun createRoute(reportId: String, saleId: String) = "counter/$reportId/$saleId"
    }
}