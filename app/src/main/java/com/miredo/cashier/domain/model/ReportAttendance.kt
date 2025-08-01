package com.miredo.cashier.domain.model

import com.miredo.cashier.data.enums.Status

data class ReportAttendance(
    val status: Status,
    val date: String,
)