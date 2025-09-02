package com.miredo.cashier.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyUtils {
    private val indonesianLocale = Locale("id", "ID")
    private val symbols = DecimalFormatSymbols(indonesianLocale).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    
    private val currencyFormat = DecimalFormat("#,##0", symbols)
    private val decimalFormat = DecimalFormat("#,##0.00", symbols)
    
    fun formatCurrency(amount: Long): String {
        return "Rp ${currencyFormat.format(amount)}"
    }
    
    fun formatCurrency(amount: Double): String {
        return "Rp ${decimalFormat.format(amount)}"
    }
    
    fun formatCurrency(amount: String): String {
        return try {
            val numericValue = amount.replace("[^0-9.,]".toRegex(), "")
                .replace(",", ".")
                .toDoubleOrNull() ?: 0.0
            formatCurrency(numericValue)
        } catch (e: Exception) {
            "Rp 0"
        }
    }
    
    fun parseCurrency(formattedAmount: String): Double {
        return try {
            formattedAmount
                .replace("Rp", "")
                .replace(".", "")
                .replace(",", ".")
                .trim()
                .toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
    
    fun formatNumber(number: String): String {
        val cleanNumber = number.replace("[^0-9]".toRegex(), "")
        return if (cleanNumber.isNotEmpty()) {
            try {
                currencyFormat.format(cleanNumber.toLong())
            } catch (e: Exception) {
                cleanNumber
            }
        } else {
            ""
        }
    }
}