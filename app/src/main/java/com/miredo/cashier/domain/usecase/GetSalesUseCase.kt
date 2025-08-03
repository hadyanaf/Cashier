package com.miredo.cashier.domain.usecase

import com.miredo.cashier.data.model.Sale
import com.miredo.cashier.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSalesUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(reportId: String): Flow<List<Sale>> {
        return repository.getSales(reportId)
    }
}