package com.miredo.cashier.presentation.screen.counter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.domain.model.MenuDetail
import com.miredo.cashier.presentation.animation.AnimationConstants
import com.miredo.cashier.presentation.animation.ExpandableAnimation
import com.miredo.cashier.presentation.animation.FadeInAnimation
import com.miredo.cashier.presentation.animation.SlideInAnimation
import com.miredo.cashier.presentation.components.CurrencyTextField
import com.miredo.cashier.presentation.components.CustomButton
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.components.PaymentMethodSelector
import com.miredo.cashier.presentation.components.TextCounter
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.presentation.ui.theme.White
import com.miredo.cashier.util.CurrencyUtils
import com.miredo.cashier.util.toRupiah

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    reportId: String,
    saleId: String? = null,
    navController: NavController,
    viewModel: CounterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var totalPaid by remember { mutableStateOf("") }
    var change by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf<PaymentType?>(null) }

    LaunchedEffect(reportId, saleId) {
        viewModel.onEvent(CounterViewModel.ViewEvent.Init(reportId, saleId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CounterViewModel.ViewEffect.ShowTotal -> totalPaid = effect.total.toRupiah()
                is CounterViewModel.ViewEffect.CalculateChange -> change = effect.change.toRupiah()
                CounterViewModel.ViewEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                CounterViewModel.ViewEffect.ShowCashLayout -> {
                    selectedMethod = PaymentType.CASH
                }

                CounterViewModel.ViewEffect.ShowQrisLayout -> {
                    selectedMethod = PaymentType.QRIS
                }

                is CounterViewModel.ViewEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    CounterScreenContent(
        modifier = modifier,
        state = state,
        totalPaid = totalPaid,
        change = change,
        selectedMethod = selectedMethod,
        snackbarHostState = snackbarHostState,
        onNavigateBack = { navController.popBackStack() },
        event = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CounterScreenContent(
    modifier: Modifier = Modifier,
    state: CounterViewModel.ViewState,
    totalPaid: String,
    change: String,
    selectedMethod: PaymentType? = null,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    event: (CounterViewModel.ViewEvent) -> Unit
) {
    var cash by remember { mutableStateOf("") }

    GradientBackground {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Kasir",
                            color = White
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = White
                            )
                        }
                    },
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = BlueTertiary,
                        titleContentColor = White,
                        navigationIconContentColor = White
                    )
                )
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Menu Items Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Pilih Menu",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextDefault
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        state.menuDetails.forEach { item ->
                            TextCounter(
                                title = item.title,
                                price = item.price,
                                count = item.count,
                                onIncrement = {
                                    event(
                                        CounterViewModel.ViewEvent.OnCountChanged(
                                            item.title,
                                            item.count + 1
                                        )
                                    )
                                },
                                onDecrement = {
                                    event(
                                        CounterViewModel.ViewEvent.OnCountChanged(
                                            item.title,
                                            (item.count - 1).coerceAtLeast(0)
                                        )
                                    )
                                },
                                onCountChange = {
                                    event(
                                        CounterViewModel.ViewEvent.OnCountChanged(
                                            item.title,
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                // Payment Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Bayar",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextDefault
                            )

                            Text(
                                text = totalPaid,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Metode Bayar",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethodSelector(
                            modifier = Modifier.fillMaxWidth(),
                            selectedMethod = selectedMethod,
                            onMethodSelected = {
                                if (it == PaymentType.CASH)
                                    event(CounterViewModel.ViewEvent.OnCashClicked)
                                else
                                    event(CounterViewModel.ViewEvent.OnQrisClicked)
                            })

                        ExpandableAnimation(visible = selectedMethod == PaymentType.CASH) {
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))

                                CurrencyTextField(
                                    value = cash,
                                    onValueChange = { newValue ->
                                        cash = newValue
                                        event(
                                            CounterViewModel.ViewEvent.OnCashChanged(
                                                newValue.toIntOrNull() ?: 0
                                            )
                                        )
                                    },
                                    label = "Uang Tunai"
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Kembalian",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Text(
                                        text = change,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (change.contains("-")) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Save Button
                CustomButton(
                    text = if (state.isEditMode) "Perbarui & Selesai" else "Simpan & Selesai",
                    onClick = { event(CounterViewModel.ViewEvent.OnSaveClicked) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterScreenPreview() {
    val state = CounterViewModel.ViewState(
        reportId = "test",
        isEditMode = false,
        menuDetails = listOf(
            MenuDetail(title = "Ori", price = 3000, count = 1),
            MenuDetail(title = "Spicy", price = 3500, count = 2),
            MenuDetail(title = "Chicken", price = 4000, count = 0)
        ),
        totalPrice = 10000,
        paymentType = PaymentType.CASH
    )

    CounterScreenContent(
        modifier = Modifier,
        state = state,
        totalPaid = "10000",
        change = "5000",
        selectedMethod = PaymentType.CASH,
        snackbarHostState = remember { SnackbarHostState() },
        onNavigateBack = {},
        event = {}
    )
}