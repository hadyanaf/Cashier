package com.miredo.cashier.presentation.screen.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.domain.model.ReportAttendance
import com.miredo.cashier.domain.usecase.GetSalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(private val getSalesUseCase: GetSalesUseCase) :
    ViewModel() {

    private val _sales = MutableStateFlow<List<ReportAttendance>>(emptyList())
    val sale: StateFlow<List<ReportAttendance>> = _sales.asStateFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Init -> onInit(event)
            is ViewEvent.OnButtonSaveClicked -> onButtonSaveClicked(event)
        }
    }

    private fun onInit(event: ViewEvent.Init) {
        getSales()
    }

    private fun getSales() {
        viewModelScope.launch {
            getSalesUseCase.invoke().collectLatest { sales ->
                _sales.value = sales
            }
        }
    }

    sealed interface ViewEvent {
        data class Init(val reportId: String) : ViewEvent
        data class OnButtonSaveClicked(
            val oil: String,
            val flour: String,
            val display: Map<Flavor, Int>,
            val raw: Map<Flavor, Int>
        ) : ViewEvent
    }

    sealed interface ViewEffect {
        data object NavigateBack : ViewEffect
    }

    data class ViewState(
        val reportId: String = ""
    )
}