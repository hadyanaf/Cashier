package com.miredo.cashier.presentation.screen.counter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.domain.model.MenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(
        ViewState(
            menuItems = listOf(
                MenuItem(title = "Ori", price = 3000),
                MenuItem(title = "Spicy", price = 3500),
                MenuItem(title = "Chicken", price = 4000)
            )
        )
    )
    val state: State<ViewState> = _state

    private val _effect = MutableSharedFlow<ViewEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnCountChanged -> onCountChanged(event)
            ViewEvent.OnSaveClicked -> onSaveClicked()
        }
    }

    private fun onCountChanged(event: ViewEvent.OnCountChanged) {
        val updatedItems = _state.value.menuItems.map {
            if (it.title == event.title) it.copy(count = event.count) else it
        }
        _state.value = _state.value.copy(menuItems = updatedItems)
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            _effect.emit(ViewEffect.NavigateBack)
        }
    }


    sealed interface ViewEvent {
        data class OnCountChanged(val title: String, val count: Int) : ViewEvent
        data object OnSaveClicked : ViewEvent
    }

    sealed interface ViewEffect {
        data object NavigateBack : ViewEffect
    }

    data class ViewState(
        val menuItems: List<MenuItem> = emptyList()
    )
}