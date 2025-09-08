package com.miredo.cashier.data.source.remote

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.Sale
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getReports(): Flow<List<AttendanceTask>>
    fun getSales(reportId: String): Flow<List<Sale>>
    suspend fun getSale(reportId: String, saleId: String): Sale?
    suspend fun createReport(date: String): String
    suspend fun saveCheckInData(id: String, data: AttendanceTask)
    suspend fun addSale(reportId: String, sale: Sale)
    suspend fun updateSale(reportId: String, saleId: String, sale: Sale)
    suspend fun deleteSale(reportId: String, saleId: String)
}