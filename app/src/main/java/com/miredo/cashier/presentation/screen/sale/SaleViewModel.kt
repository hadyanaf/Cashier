package com.miredo.cashier.presentation.screen.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.domain.model.SaleDomain
import com.miredo.cashier.domain.usecase.DeleteSaleUseCase
import com.miredo.cashier.domain.usecase.GetSalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    private val getSalesUseCase: GetSalesUseCase,
    private val deleteSaleUseCase: DeleteSaleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ViewEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Init -> onInit(event)
            is ViewEvent.OnAddSaleClicked -> onAddSaleClicked()
            is ViewEvent.OnSaleClicked -> onSaleClicked(event)
            is ViewEvent.OnDeleteSaleClicked -> onDeleteSaleClicked(event)
            is ViewEvent.OnSaveAndContinueClicked -> onSaveAndContinueClicked()
        }
    }

    private fun onInit(event: ViewEvent.Init) {
        _state.value = _state.value.copy(reportId = event.reportId)
        getSales(event.reportId)
    }

    private fun getSales(reportId: String) {
        viewModelScope.launch {
            getSalesUseCase.invoke(reportId).collectLatest { sales ->
                _state.value = _state.value.copy(sales = sales)
            }
        }
    }

    private fun onAddSaleClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.NavigateToCounter(reportId = _state.value.reportId, saleId = null))
        }
    }

    private fun onSaleClicked(event: ViewEvent.OnSaleClicked) {
        viewModelScope.launch {
            _effect.emit(ViewEffect.NavigateToCounter(reportId = _state.value.reportId, saleId = event.saleId))
        }
    }

    private fun onDeleteSaleClicked(event: ViewEvent.OnDeleteSaleClicked) {
        viewModelScope.launch {
            try {
                deleteSaleUseCase.invoke(_state.value.reportId, event.saleId)
            } catch (e: Exception) {
                _effect.emit(ViewEffect.ShowError("Failed to delete sale: ${e.message}"))
            }
        }
    }

    private fun onSaveAndContinueClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.NavigateToCheckout(reportId = _state.value.reportId))
        }
    }

    sealed interface ViewEvent {
        data class Init(val reportId: String) : ViewEvent
        data object OnAddSaleClicked : ViewEvent
        data class OnSaleClicked(val saleId: String) : ViewEvent
        data class OnDeleteSaleClicked(val saleId: String) : ViewEvent
        data object OnSaveAndContinueClicked : ViewEvent
    }

    sealed interface ViewEffect {
        data object NavigateBack : ViewEffect
        data class NavigateToCounter(val reportId: String, val saleId: String?) : ViewEffect
        data class NavigateToCheckout(val reportId: String) : ViewEffect
        data class ShowError(val message: String) : ViewEffect
    }

    data class ViewState(
        val reportId: String = "",
        val sales: List<SaleDomain> = emptyList(),
        val isLoading: Boolean = false
    )
}