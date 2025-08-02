package com.miredo.cashier.data.model

import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.Flavor

data class CheckData(
    val timestamp: Timestamp,
    val displayStock: Map<Flavor, Int>,
    val rawStock: Map<Flavor, Int>,
    val ingredients: Map<String, Float>
)