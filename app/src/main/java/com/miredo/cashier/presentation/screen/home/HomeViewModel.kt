package com.miredo.cashier.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miredo.cashier.domain.model.ReportAttendance
import com.miredo.cashier.domain.usecase.GetAttendanceReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAttendanceReportsUseCase: GetAttendanceReportsUseCase) :
    ViewModel() {
    private val _reports = MutableStateFlow<List<ReportAttendance>>(emptyList())
    val reports: StateFlow<List<ReportAttendance>> = _reports.asStateFlow()

    init {
        getAttendanceReports()
    }

    private fun getAttendanceReports() {
        viewModelScope.launch {
            getAttendanceReportsUseCase.invoke().collectLatest { reports ->
                _reports.value = reports
            }
        }
    }
}