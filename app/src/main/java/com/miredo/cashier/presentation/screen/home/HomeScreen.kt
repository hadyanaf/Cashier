package com.miredo.cashier.presentation.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miredo.cashier.data.enums.Status
import com.miredo.cashier.domain.model.ReportAttendance
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.components.ReportItem
import com.miredo.cashier.presentation.components.RoundedTopAppBar
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToSale: (String) -> Unit,
    onSignOut: (() -> Unit)? = null,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val reports by viewModel.reports.collectAsState()

    HomeScreenContent(
        modifier = modifier,
        reports = reports,
        onAddClicked = onNavigateToCheckIn,
        onItemClicked = onNavigateToSale,
        onSignOut = onSignOut
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    reports: List<ReportAttendance> = emptyList(),
    onAddClicked: () -> Unit,
    onItemClicked: (String) -> Unit = {},
    onSignOut: (() -> Unit)? = null
) {
    GradientBackground {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            item {
                RoundedTopAppBar(
                    title = "Laporan Harian",
                    actions = {
                        if (onSignOut != null) {
                            IconButton(onClick = onSignOut) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Keluar",
                                    tint = White
                                )
                            }
                        }
                    }
                )
            }

            item {
                Card(
                    onClick = onAddClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = White,
                        contentColor = BluePrimary
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = BluePrimary.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tambah",
                            tint = BluePrimary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Tambah Laporan Hari Ini",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = BluePrimary
                        )
                    }
                }
            }

            items(reports) { report ->
                ReportItem(
                    date = report.date,
                    status = report.status,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable {
                            onItemClicked(report.id)
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenContentPreview() {
    val reports = listOf(
        ReportAttendance(
            id = "1",
            status = Status.IN_PROGRESS,
            date = "Selasa, 22 September 2025"
        ),
        ReportAttendance(
            id = "2",
            status = Status.CHECKED_IN,
            date = "Senin, 21 September 2025"

        )

    )
    HomeScreenContent(modifier = Modifier, reports = reports, onAddClicked = {})
}

