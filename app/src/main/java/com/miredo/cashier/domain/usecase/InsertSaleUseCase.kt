package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.Sale
import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class InsertSaleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(reportId: String, sale: Sale) {
        repository.addSale(reportId = reportId, sale = sale)
    }
}