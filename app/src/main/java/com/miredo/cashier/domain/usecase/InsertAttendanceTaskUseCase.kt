package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class InsertAttendanceTaskUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(date: String, task: AttendanceTask) {
        repository.saveCheckInData(date,task)
    }
}