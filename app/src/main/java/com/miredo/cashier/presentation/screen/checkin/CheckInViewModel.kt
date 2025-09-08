package com.miredo.cashier.presentation.screen.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.Ingredient
import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.data.model.AttendanceTask
import com.miredo.cashier.data.model.CheckData
import com.miredo.cashier.domain.usecase.CreateReportUseCase
import com.miredo.cashier.domain.usecase.InsertAttendanceTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val createReportUseCase: CreateReportUseCase,
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
            val date = LocalDate.now().toString()
            
            val ingredients = hashMapOf(
                Ingredient.OIL.name to (event.oil.toFloatOrNull() ?: 0f),
                Ingredient.FLOUR.name to (event.flour.toFloatOrNull() ?: 0f)
            )

            val checkData = CheckData(
                timestamp = Timestamp.now(),
                displayStock = event.display.mapKeys { it.key.name },
                rawStock = event.raw.mapKeys { it.key.name },
                ingredients = ingredients
            )

            val attendanceTask = AttendanceTask(
                id = date, // Use date directly as ID
                userId = "", // Will be set by the use case
                date = date,
                checkIn = checkData,
                checkOut = null,
                status = Status.CHECKED_IN
            )

            insertAttendanceTaskUseCase(
                reportId = date, // Use date as report ID
                task = attendanceTask
            )

            _effect.emit(ViewEffect.NavigateBack)
        }
    }

    sealed interface ViewEvent {
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
}