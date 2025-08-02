package com.miredo.cashier.domain.model

data class MenuDetail(
    val title: String,
    val price: Int,
    val count: Int = 0
) {
    val totalPrice: Int get() = count * price
}
