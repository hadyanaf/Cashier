package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAttendanceTaskUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(reportId: String): AttendanceTask? {
        return repository.getReports().first().find { it.id == reportId }
    }
}