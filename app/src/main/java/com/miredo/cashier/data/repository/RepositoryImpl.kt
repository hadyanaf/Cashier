package com.miredo.cashier.data.repository

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.source.remote.RemoteDataSource
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    Repository {

    override fun getReports(): Flow<List<AttendanceTask>> = remoteDataSource.getReports()

    override suspend fun saveCheckInData(
        id: String,
        data: AttendanceTask
    ) = remoteDataSource.saveCheckInData(id, data)
}