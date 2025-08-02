package com.miredo.cashier.presentation.screen.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.domain.model.MenuDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(
        ViewState(
            menuDetails = listOf(
                MenuDetail(title = "Ori", price = 3000),
                MenuDetail(title = "Spicy", price = 3500),
                MenuDetail(title = "Chicken", price = 4000)
            )
        )
    )
    val state: StateFlow<ViewState> = _state

    private val _effect = MutableSharedFlow<ViewEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnCountChanged -> onCountChanged(event)
            is ViewEvent.OnCashChanged -> onCashChanged(event)
            ViewEvent.OnSaveClicked -> onSaveClicked()
            ViewEvent.OnCashClicked -> onCashClicked()
            ViewEvent.OnQrisClicked -> onQrisClicked()
        }
    }

    private fun onCountChanged(event: ViewEvent.OnCountChanged) {
        viewModelScope.launch {
            val updatedItems = _state.value.menuDetails.map {
                if (it.title == event.title) it.copy(count = event.count) else it
            }
            _state.value = _state.value.copy(menuDetails = updatedItems)
            _effect.emit(ViewEffect.ShowTotal(updatedItems.sumOf { it.totalPrice }))
        }
    }

    private fun onCashChanged(event: ViewEvent.OnCashChanged) {
        viewModelScope.launch {
            val change = event.cash - _state.value.menuDetails.sumOf { it.totalPrice }
            _effect.emit(ViewEffect.CalculateChange(change))
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.NavigateBack)
        }
    }

    private fun onCashClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.ShowCashLayout)
        }
    }

    private fun onQrisClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.ShowQrisLayout)
        }
    }

    sealed interface ViewEvent {
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
    }

    data class ViewState(
        val menuDetails: List<MenuDetail> = emptyList()
    )
}