package com.miredo.cashier.domain.repository

import com.miredo.cashier.data.model.AttendanceTask
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getReports(): Flow<List<AttendanceTask>>
    suspend fun saveCheckInData(id: String, data: AttendanceTask)
}