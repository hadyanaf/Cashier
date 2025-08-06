package com.miredo.cashier.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.domain.model.ReportAttendance
import com.miredo.cashier.presentation.components.ReportItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToSale: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsState()

    HomeScreenContent(modifier = modifier, reports = reports, onAddClicked = onNavigateToCheckIn, onItemClicked = onNavigateToSale)
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    reports: List<ReportAttendance> = emptyList(),
    onAddClicked: () -> Unit,
    onItemClicked: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClicked() },
                shape = MaterialTheme.shapes.extraLarge
            )
            { Icon(Icons.Filled.Add, "Add Button") }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 12.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {

            items(reports) { report ->
                ReportItem(date = report.date, status = report.status, modifier = Modifier)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    val reports = listOf(
        ReportAttendance(
            status = Status.IN_PROGRESS,
            date = "Selasa, 22 September 2025"
        ),
        ReportAttendance(
            status = Status.CHECKED_IN,
            date = "Senin, 21 September 2025"

        )

    )
    HomeScreenContent(modifier = Modifier, reports = reports, onAddClicked = {})
}

