package com.miredo.cashier.data.model

import com.google.firebase.Timestamp

data class CheckData(
    val timestamp: Timestamp = Timestamp.now(),
    val displayStock: Map<String, Int> = emptyMap(),
    val rawStock: Map<String, Int> = emptyMap(),
    val ingredients: Map<String, Float> = emptyMap()
)