package com.miredo.cashier.presentation.screen.counter

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.domain.model.MenuDetail
import com.miredo.cashier.presentation.components.PaymentMethodSelector
import com.miredo.cashier.presentation.components.TextCounter
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.util.toRupiah


@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var totalPaid by remember { mutableStateOf("") }
    var change by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf<PaymentType?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CounterViewModel.ViewEffect.ShowTotal -> totalPaid = effect.total.toRupiah()
                is CounterViewModel.ViewEffect.CalculateChange -> change = effect.change.toRupiah()
                CounterViewModel.ViewEffect.NavigateBack -> saveButtonClicked()
                CounterViewModel.ViewEffect.ShowCashLayout -> {
                    selectedMethod = PaymentType.CASH
                }

                CounterViewModel.ViewEffect.ShowQrisLayout -> {
                    selectedMethod = PaymentType.QRIS
                }
            }
        }
    }

    CounterScreenContent(
        modifier = modifier,
        menuDetails = state.menuDetails,
        totalPaid = totalPaid,
        change = change,
        selectedMethod = selectedMethod,
        event = viewModel::onEvent
    )
}

@Composable
private fun CounterScreenContent(
    modifier: Modifier = Modifier,
    menuDetails: List<MenuDetail>,
    totalPaid: String,
    change: String,
    selectedMethod: PaymentType? = null,
    event: (CounterViewModel.ViewEvent) -> Unit
) {
    var cash by remember { mutableStateOf("") }

    Scaffold(modifier = modifier.padding(top = 20.dp), bottomBar = {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = { saveButtonClicked() }
        ) {
            Text("Simpan & Selesai")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(12.dp)
            ) {
                menuDetails.forEach { item ->
                    TextCounter(
                        title = item.title,
                        price = item.price,
                        count = item.count,
                        onIncrement = {
                            Log.d("Error", "Check click increment")
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
                            Log.d("Error", "Check count")
                            event(
                                CounterViewModel.ViewEvent.OnCountChanged(
                                    item.title,
                                    it
                                )
                            )
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Bayar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDefault,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = totalPaid,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextDefault,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Metode Bayar",
                style = MaterialTheme.typography.bodyMedium,
                color = TextDefault,
                modifier = Modifier.padding(top = 12.dp)
            )

            PaymentMethodSelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                selectedMethod = selectedMethod,
                onMethodSelected = {
                    if (it == PaymentType.CASH)
                        event(CounterViewModel.ViewEvent.OnCashClicked)
                    else
                        event(CounterViewModel.ViewEvent.OnQrisClicked)
                })

            if (selectedMethod == PaymentType.CASH) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    value = cash,
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        if (filtered.length <= 6) cash = filtered
                        event(CounterViewModel.ViewEvent.OnCashChanged(cash.toIntOrNull() ?: 0))
                    },
                    label = { Text("Uang Tunai") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kembalian",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDefault,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = change,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextDefault,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private fun saveButtonClicked() {

}

@Preview(showBackground = true)
@Composable
private fun CounterScreenPreview() {
    val menuDetails = listOf(
        MenuDetail(title = "Ori", price = 3000),
        MenuDetail(title = "Spicy", price = 3500),
        MenuDetail(title = "Chicken", price = 4000)
    )

    CounterScreenContent(
        modifier = Modifier,
        menuDetails = menuDetails,
        totalPaid = "9500",
        change = "10000",
        selectedMethod = PaymentType.CASH,
        event = {})
}