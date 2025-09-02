package com.miredo.cashier.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null
)