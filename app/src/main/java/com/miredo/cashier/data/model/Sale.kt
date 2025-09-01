package com.miredo.cashier.data.model

import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.domain.model.SaleDomain

data class Sale(
    val id: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val paymentType: PaymentType? = null,
    val items: Map<String, Int> = emptyMap(),
    val totalPrice: Int = 0,
    val cash: Int = 0,
    val change: Int = 0
) {
    fun toDomain() = SaleDomain(
        id = id.orEmpty(),
        paymentType = paymentType,
        items = items,
        totalPrice = totalPrice,
        cash = cash,
        change = change
    )
}