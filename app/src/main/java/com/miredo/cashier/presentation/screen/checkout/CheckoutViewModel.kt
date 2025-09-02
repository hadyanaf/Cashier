package com.miredo.cashier.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.Ingredient
import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.CheckData
import com.miredo.cashier.domain.usecase.GetAttendanceTaskUseCase
import com.miredo.cashier.domain.usecase.InsertAttendanceTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getAttendanceTaskUseCase: GetAttendanceTaskUseCase,
    private val insertAttendanceTaskUseCase: InsertAttendanceTaskUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<ViewEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnButtonSaveClicked -> onButtonSaveClicked(event)
        }
    }

    private fun onButtonSaveClicked(event: ViewEvent.OnButtonSaveClicked) {
        viewModelScope.launch {
            try {
                // Get existing attendance task to preserve checkIn data
                val existingTask = getAttendanceTaskUseCase(event.reportId)
                
                // Create ingredients map
                val ingredients = hashMapOf(
                    Ingredient.OIL.name to (event.oil.toFloatOrNull() ?: 0f),
                    Ingredient.FLOUR.name to (event.flour.toFloatOrNull() ?: 0f)
                )
                
                // Create additional resources map
                val additionalResources = event.others.associate { it.name to it.price }
                
                // Create checkout data
                val checkOutData = CheckData(
                    timestamp = Timestamp.now(),
                    displayStock = event.display.mapKeys { it.key.name },
                    rawStock = event.raw.mapKeys { it.key.name },
                    ingredients = ingredients,
                    additionalResources = additionalResources
                )
                
                // Update attendance task with checkout data
                val updatedTask = (existingTask ?: AttendanceTask(date = event.reportId)).copy(
                    checkOut = checkOutData,
                    status = Status.CHECKED_OUT
                )
                
                // Save updated task
                insertAttendanceTaskUseCase(
                    date = event.reportId,
                    task = updatedTask
                )
                
                _effect.emit(ViewEffect.NavigateToHome)
            } catch (_: Exception) {
                // Handle error if needed
                _effect.emit(ViewEffect.NavigateBack)
            }
        }
    }

    sealed interface ViewEvent {
        data class OnButtonSaveClicked(
            val reportId: String,
            val oil: String,
            val flour: String,
            val display: Map<Flavor, Int>,
            val raw: Map<Flavor, Int>,
            val others: List<AdditionalResource>
        ) : ViewEvent
    }

    sealed interface ViewEffect {
        data object NavigateBack : ViewEffect
        data object NavigateToHome : ViewEffect
    }

    data class AdditionalResource(
        val name: String,
        val price: String
    )
}