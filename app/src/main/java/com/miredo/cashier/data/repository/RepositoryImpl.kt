package com.miredo.cashier.data.repository

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.Sale
import com.miredo.cashier.data.source.remote.RemoteDataSource
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    Repository {

    override fun getReports(): Flow<List<AttendanceTask>> = remoteDataSource.getReports()

    override fun getSales(reportId: String): Flow<List<Sale>> = remoteDataSource.getSales(reportId)

    override suspend fun createReport(date: String): String = remoteDataSource.createReport(date)

    override suspend fun saveCheckInData(
        id: String,
        data: AttendanceTask
    ) = remoteDataSource.saveCheckInData(id, data)

    override suspend fun getSale(reportId: String, saleId: String): Sale? =
        remoteDataSource.getSale(reportId, saleId)

    override suspend fun addSale(
        reportId: String,
        sale: Sale
    ) = remoteDataSource.addSale(reportId, sale)

    override suspend fun updateSale(
        reportId: String,
        saleId: String,
        sale: Sale
    ) = remoteDataSource.updateSale(reportId, saleId, sale)

    override suspend fun deleteSale(
        reportId: String,
        saleId: String
    ) = remoteDataSource.deleteSale(reportId, saleId)
}