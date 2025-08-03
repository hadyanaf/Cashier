package com.miredo.cashier.data.model

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CheckIn: Screen("check_in")
    object Sale: Screen("sale")
    object Counter : Screen("counter")
}