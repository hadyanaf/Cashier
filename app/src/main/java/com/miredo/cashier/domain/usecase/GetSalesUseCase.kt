package com.miredo.cashier.domain.usecase

import com.miredo.cashier.domain.model.SaleDomain
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(reportId: String): Flow<List<SaleDomain>> {
        val sales = repository.getSales(reportId).map { sale -> sale.map { it.toDomain() } }
        return sales
    }
}