package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.Sale
import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class UpdateSaleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(reportId: String, saleId: String, sale: Sale) {
        repository.updateSale(reportId = reportId, saleId = saleId, sale = sale)
    }
}