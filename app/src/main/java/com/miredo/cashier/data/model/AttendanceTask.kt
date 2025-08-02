package com.miredo.cashier.data.model

import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.domain.model.ReportAttendance

data class AttendanceTask(
    val date: String = "",
    val checkIn: CheckData? = null,
    val checkOut: CheckData? = null,
    val status: Status? = null
) {
    fun toReportAttendance() = ReportAttendance(
        date = date,
        status = status ?: Status.IN_PROGRESS,
    )
}