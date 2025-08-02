package com.miredo.cashier.data.source.remote

import com.miredo.cashier.data.model.AttendanceTask
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getReports(): Flow<List<AttendanceTask>>
    suspend fun saveCheckInData(id: String, data: AttendanceTask)
}