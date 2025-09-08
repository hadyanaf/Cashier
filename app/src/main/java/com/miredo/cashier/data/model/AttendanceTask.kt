package com.miredo.cashier.data.model

import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.domain.model.ReportAttendance

data class AttendanceTask(
    val id: String = "",
    val userId: String = "",
    val date: String = "",
    val checkIn: CheckData? = null,
    val checkOut: CheckData? = null,
    val status: Status? = null
) {
    fun toReportAttendance() = ReportAttendance(
        id = id,
        date = date,
        status = status ?: Status.IN_PROGRESS,
    )
    
    companion object {
        fun create(
            userId: String,
            date: String,
            checkIn: CheckData? = null,
            checkOut: CheckData? = null,
            status: Status = Status.IN_PROGRESS
        ): AttendanceTask {
            return AttendanceTask(
                id = date, // Use date as ID directly
                userId = userId,
                date = date,
                checkIn = checkIn,
                checkOut = checkOut,
                status = status
            )
        }
    }
}