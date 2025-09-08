package com.miredo.cashier.presentation.screen.sale

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.data.model.Screen
import com.miredo.cashier.domain.model.SaleDomain
import com.miredo.cashier.presentation.animation.AnimationConstants
import com.miredo.cashier.presentation.animation.FadeInAnimation
import com.miredo.cashier.presentation.components.CustomButton
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.components.RoundedTopAppBar
import com.miredo.cashier.presentation.components.SaleItem
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun SaleScreen(
    modifier: Modifier = Modifier,
    reportId: String,
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(reportId) {
        viewModel.onEvent(SaleViewModel.ViewEvent.Init(reportId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SaleViewModel.ViewEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is SaleViewModel.ViewEffect.NavigateToCounter -> {
                    val route = if (effect.saleId != null) {
                        Screen.CounterEdit.createRoute(effect.reportId, effect.saleId)
                    } else {
                        Screen.Counter.createRoute(effect.reportId)
                    }
                    navController.navigate(route)
                }

                is SaleViewModel.ViewEffect.NavigateToCheckout -> {
                    navController.navigate(Screen.CheckOut.createRoute(effect.reportId))
                }

                is SaleViewModel.ViewEffect.ShowError -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    SaleScreenContent(
        modifier = modifier,
        state = state,
        snackbarHostState = snackBarHostState,
        onNavigateBack = { navController.popBackStack() },
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreenContent(
    modifier: Modifier = Modifier,
    state: SaleViewModel.ViewState,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onEvent: (SaleViewModel.ViewEvent) -> Unit
) {
    GradientBackground {
        Box(modifier = modifier) {
            Column {
                RoundedTopAppBar(
                    title = "Rekap Penjualan",
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = White
                            )
                        }
                    }
                )

                // Add Item Button
                Card(
                    onClick = { onEvent(SaleViewModel.ViewEvent.OnAddSaleClicked) },
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
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah",
                            tint = BluePrimary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Tambah Penjualan",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = BluePrimary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxSize()
                ) {
                    if (state.sales.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada penjualan. Tap tombol 'Tambah Item Counter' untuk menambah penjualan baru.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            val sortedSales = state.sales.sortedByDescending { it.createdAt.seconds }
                            items(sortedSales) { sale ->
                                FadeInAnimation(
                                    visible = true,
                                    delayMillis = sortedSales.indexOf(sale) * AnimationConstants.DELAY_SHORT
                                ) {
                                    SaleItem(
                                        modifier = Modifier,
                                        totalPrice = sale.totalPrice,
                                        items = sale.items,
                                        paymentType = sale.paymentType ?: PaymentType.CASH,
                                        createdAt = sale.createdAt,
                                        onClick = {
                                            onEvent(SaleViewModel.ViewEvent.OnSaleClicked(sale.id))
                                        },
                                        onDeleteClick = {
                                            onEvent(SaleViewModel.ViewEvent.OnDeleteSaleClicked(sale.id))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Save & Continue Button
                    CustomButton(
                        text = "Simpan & Lanjutkan",
                        onClick = { onEvent(SaleViewModel.ViewEvent.OnSaveAndContinueClicked) },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview
@Composable
private fun SaleScreenContentPreview() {
    val state = SaleViewModel.ViewState(
        reportId = "test",
        sales = listOf(
            SaleDomain(
                id = "sale1",
                createdAt = Timestamp.now(),
                paymentType = PaymentType.CASH,
                items = mapOf("Ori" to 2, "Spicy" to 1),
                totalPrice = 9500
            ),
            SaleDomain(
                id = "sale2",
                createdAt = Timestamp(Timestamp.now().seconds - 300, 0), // 5 minutes ago
                paymentType = PaymentType.QRIS,
                items = mapOf("Chicken" to 1),
                totalPrice = 4000
            )
        )
    )
    SaleScreenContent(
        state = state,
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateBack = {},
        onEvent = {}
    )
}