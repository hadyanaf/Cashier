package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class CreateReportUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(date: String): String {
        return repository.createReport(date)
    }
}