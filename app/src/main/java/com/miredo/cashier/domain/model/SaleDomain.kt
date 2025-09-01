package com.miredo.cashier.domain.model

import com.miredo.cashier.data.enums.PaymentType

data class SaleDomain(
    val id: String = "",
    val paymentType: PaymentType? = null,
    val items: Map<String, Int> = emptyMap(),
    val totalPrice: Int = 0,
    val cash: Int = 0,
    val change: Int = 0
)