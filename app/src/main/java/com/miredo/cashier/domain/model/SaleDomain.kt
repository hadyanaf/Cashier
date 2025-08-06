package com.miredo.cashier.domain.model

import com.miredo.cashier.data.enums.PaymentType

data class SaleDomain(
    val paymentType: PaymentType? = null,
    val items: Map<String, Int> = emptyMap(),
    val totalPrice: Int = 0,
)