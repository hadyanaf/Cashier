package com.miredo.cashier.presentation.screen.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.data.model.Sale
import com.miredo.cashier.domain.model.MenuDetail
import com.miredo.cashier.domain.usecase.GetSingleSaleUseCase
import com.miredo.cashier.domain.usecase.InsertSaleUseCase
import com.miredo.cashier.domain.usecase.UpdateSaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val getSingleSaleUseCase: GetSingleSaleUseCase,
    private val insertSaleUseCase: InsertSaleUseCase,
    private val updateSaleUseCase: UpdateSaleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> = _state

    private val _effect = MutableSharedFlow<ViewEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Init -> onInit(event)
            is ViewEvent.OnCountChanged -> onCountChanged(event)
            is ViewEvent.OnCashChanged -> onCashChanged(event)
            ViewEvent.OnSaveClicked -> onSaveClicked()
            ViewEvent.OnCashClicked -> onCashClicked()
            ViewEvent.OnQrisClicked -> onQrisClicked()
        }
    }

    private fun onInit(event: ViewEvent.Init) {
        _state.value = _state.value.copy(
            reportId = event.reportId,
            saleId = event.saleId,
            isEditMode = event.saleId != null,
            menuDetails = createInitialMenuDetails()
        )

        // Load existing sale data if in edit mode
        if (event.saleId != null) {
            loadSaleData(event.reportId, event.saleId)
        }

        // Calculate initial total
        calculateTotal()
    }

    private fun createInitialMenuDetails(): List<MenuDetail> {
        return Flavor.entries.map { flavor ->
            MenuDetail(
                title = flavor.label,
                price = when (flavor) {
                    Flavor.ORI -> 3000
                    Flavor.SPICY -> 3500
                    Flavor.CHICKEN -> 4000
                },
                count = 0
            )
        }
    }

    private fun loadSaleData(reportId: String, saleId: String) {
        viewModelScope.launch {
            try {
                val saleDomain = getSingleSaleUseCase.invoke(reportId, saleId)
                if (saleDomain != null) {
                    val updatedMenuDetails = _state.value.menuDetails.map { menuItem ->
                        val count = saleDomain.items[menuItem.title] ?: 0
                        menuItem.copy(count = count)
                    }
                    _state.value = _state.value.copy(
                        menuDetails = updatedMenuDetails,
                        paymentType = saleDomain.paymentType,
                        cash = saleDomain.cash
                    )
                    calculateTotal()
                    if (saleDomain.paymentType == PaymentType.CASH) {
                        _effect.emit(ViewEffect.ShowCashLayout)
                    } else {
                        _effect.emit(ViewEffect.ShowQrisLayout)
                    }
                }
            } catch (e: Exception) {
                _effect.emit(ViewEffect.ShowError("Failed to load sale data: ${e.message}"))
            }
        }
    }

    private fun onCountChanged(event: ViewEvent.OnCountChanged) {
        viewModelScope.launch {
            val updatedItems = _state.value.menuDetails.map {
                if (it.title == event.title) it.copy(count = event.count) else it
            }
            _state.value = _state.value.copy(menuDetails = updatedItems)
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        viewModelScope.launch {
            val total = _state.value.menuDetails.sumOf { it.totalPrice }
            _state.value = _state.value.copy(totalPrice = total)
            _effect.emit(ViewEffect.ShowTotal(total))

            // Recalculate change if cash payment
            if (_state.value.paymentType == PaymentType.CASH && _state.value.cash > 0) {
                val change = _state.value.cash - total
                _effect.emit(ViewEffect.CalculateChange(change))
            }
        }
    }

    private fun onCashChanged(event: ViewEvent.OnCashChanged) {
        viewModelScope.launch {
            _state.value = _state.value.copy(cash = event.cash)
            val change = event.cash - _state.value.totalPrice
            _effect.emit(ViewEffect.CalculateChange(change))
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            try {
                val items = _state.value.menuDetails.associate { it.title to it.count }
                    .filterValues { it > 0 } // Only include items with count > 0

                if (items.isEmpty()) {
                    _effect.emit(ViewEffect.ShowError("Please add at least one item"))
                    return@launch
                }

                if (_state.value.paymentType == null) {
                    _effect.emit(ViewEffect.ShowError("Please select a payment method"))
                    return@launch
                }

                val sale = Sale(
                    id = _state.value.saleId,
                    createdAt = Timestamp.now(),
                    paymentType = _state.value.paymentType,
                    items = items,
                    totalPrice = _state.value.totalPrice,
                    cash = if (_state.value.paymentType == PaymentType.CASH) _state.value.cash else 0,
                    change = if (_state.value.paymentType == PaymentType.CASH) _state.value.cash - _state.value.totalPrice else 0
                )

                if (_state.value.isEditMode && _state.value.saleId != null) {
                    updateSaleUseCase.invoke(_state.value.reportId, _state.value.saleId!!, sale)
                } else {
                    insertSaleUseCase.invoke(_state.value.reportId, sale)
                }

                _effect.emit(ViewEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.emit(ViewEffect.ShowError("Failed to save sale: ${e.message}"))
            }
        }
    }

    private fun onCashClicked() {
        viewModelScope.launch {
            _state.value = _state.value.copy(paymentType = PaymentType.CASH)
            _effect.emit(ViewEffect.ShowCashLayout)
        }
    }

    private fun onQrisClicked() {
        viewModelScope.launch {
            _state.value = _state.value.copy(paymentType = PaymentType.QRIS, cash = 0)
            _effect.emit(ViewEffect.ShowQrisLayout)
        }
    }

    sealed interface ViewEvent {
        data class Init(val reportId: String, val saleId: String? = null) : ViewEvent
        data class OnCountChanged(val title: String, val count: Int) : ViewEvent
        data class OnCashChanged(val cash: Int) : ViewEvent
        data object OnSaveClicked : ViewEvent
        data object OnCashClicked : ViewEvent
        data object OnQrisClicked : ViewEvent
    }

    sealed interface ViewEffect {
        data class ShowTotal(val total: Int) : ViewEffect
        data class CalculateChange(val change: Int) : ViewEffect
        data object NavigateBack : ViewEffect
        data object ShowCashLayout : ViewEffect
        data object ShowQrisLayout : ViewEffect
        data class ShowError(val message: String) : ViewEffect
    }

    data class ViewState(
        val reportId: String = "",
        val saleId: String? = null,
        val isEditMode: Boolean = false,
        val menuDetails: List<MenuDetail> = emptyList(),
        val totalPrice: Int = 0,
        val paymentType: PaymentType? = null,
        val cash: Int = 0,
        val isLoading: Boolean = false
    )
}