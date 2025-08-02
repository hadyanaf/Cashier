package com.miredo.cashier.data.model

import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.PaymentType

data class Sale(
    val createdAt: Timestamp = Timestamp.now(),
    val paymentType: PaymentType,
    val items: Map<String, Int> = emptyMap(),
    val totalPrice: Int,
    val cash: Int,
    val change: Int
)