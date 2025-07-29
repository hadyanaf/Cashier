package com.miredo.cashier.util

import java.text.NumberFormat
import java.util.Locale

fun Int.toRupiah(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}