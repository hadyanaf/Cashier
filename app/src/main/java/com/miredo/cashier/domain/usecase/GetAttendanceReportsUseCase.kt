package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.model.ReportAttendance
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAttendanceReportsUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<List<ReportAttendance>> {
        return repository.getReports().map { reports -> reports.map { it.toReportAttendance() } }
    }
}