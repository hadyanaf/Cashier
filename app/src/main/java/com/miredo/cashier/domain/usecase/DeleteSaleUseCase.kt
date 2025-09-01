package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class DeleteSaleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(reportId: String, saleId: String) {
        repository.deleteSale(reportId = reportId, saleId = saleId)
    }
}