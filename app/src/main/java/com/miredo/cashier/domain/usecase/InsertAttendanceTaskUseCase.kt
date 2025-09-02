package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class InsertAttendanceTaskUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(date: String, task: AttendanceTask) {
        val checkIn = task.checkIn?.copy(
            displayStock = task.checkIn.displayStock.mapKeys { it.key },
            rawStock = task.checkIn.rawStock.mapKeys { it.key }
        )
        val checkOut = task.checkOut?.copy(
            displayStock = task.checkOut.displayStock.mapKeys { it.key },
            rawStock = task.checkOut.rawStock.mapKeys { it.key }
        )
        val firestoreTask = task.copy(
            checkIn = checkIn,
            checkOut = checkOut
        )
        repository.saveCheckInData(date, firestoreTask)
    }
}