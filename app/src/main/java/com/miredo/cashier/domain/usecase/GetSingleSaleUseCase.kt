package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.model.SaleDomain
import com.miredo.cashier.domain.repository.Repository
import javax.inject.Inject

class GetSingleSaleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(reportId: String, saleId: String): SaleDomain? {
        return repository.getSale(reportId = reportId, saleId = saleId)?.toDomain()
    }
}