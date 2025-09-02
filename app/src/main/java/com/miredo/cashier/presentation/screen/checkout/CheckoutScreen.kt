package com.miredo.cashier.presentation.screen.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.Ingredient
import com.miredo.cashier.data.model.Screen
import com.miredo.cashier.presentation.components.FlavorsInputRow
import com.miredo.cashier.presentation.ui.theme.TextDefault
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    reportId: String,
    navController: NavController,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val todayDate by remember {
        mutableStateOf(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern(
                    "EEEE, dd MMMM yyyy",
                    Locale.forLanguageTag("id-ID")
                )
            )
        )
    }

    var oil by remember { mutableStateOf("") }
    var flour by remember { mutableStateOf("") }

    var display by remember {
        mutableStateOf(Flavor.entries.associateWith { 0 })
    }

    var raw by remember {
        mutableStateOf(Flavor.entries.associateWith { 0 })
    }

    val others = remember { mutableStateListOf<CheckoutViewModel.AdditionalResource>() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CheckoutViewModel.ViewEffect.NavigateBack -> navController.popBackStack()
                CheckoutViewModel.ViewEffect.NavigateToHome -> {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            }
        }
    }

    CheckoutScreenContent(
        modifier = modifier,
        todayDate = todayDate,
        oil = oil,
        flour = flour,
        display = display,
        raw = raw,
        others = others,
        onOilChanged = { oil = it },
        onFlourChanged = { flour = it },
        onDisplayChanged = { flavor, count ->
            display = display.toMutableMap().apply { put(flavor, count) }
        },
        onRawChanged = { flavor, count ->
            raw = raw.toMutableMap().apply { put(flavor, count) }
        },
        onAddOther = { others.add(CheckoutViewModel.AdditionalResource("", "")) },
        onRemoveOther = { index -> others.removeAt(index) },
        onOtherNameChanged = { index, name ->
            if (index < others.size) {
                others[index] = others[index].copy(name = name)
            }
        },
        onOtherPriceChanged = { index, price ->
            if (index < others.size) {
                others[index] = others[index].copy(price = price)
            }
        },
        onSaveClicked = {
            saveButtonClicked(
                reportId = reportId,
                oil = oil,
                flour = flour,
                display = display,
                raw = raw,
                others = others,
                viewModel = viewModel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreenContent(
    modifier: Modifier = Modifier,
    todayDate: String,
    oil: String,
    flour: String,
    display: Map<Flavor, Int>,
    raw: Map<Flavor, Int>,
    others: List<CheckoutViewModel.AdditionalResource>,
    onOilChanged: (String) -> Unit,
    onFlourChanged: (String) -> Unit,
    onDisplayChanged: (Flavor, Int) -> Unit,
    onRawChanged: (Flavor, Int) -> Unit,
    onAddOther: () -> Unit,
    onRemoveOther: (Int) -> Unit,
    onOtherNameChanged: (Int, String) -> Unit,
    onOtherPriceChanged: (Int, String) -> Unit,
    onSaveClicked: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Check Out") }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onClick = { onSaveClicked() }
            ) {
                Text("Simpan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Hari/Tanggal",
                color = TextDefault
            )

            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = todayDate,
                color = TextDefault
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Stok Tahu",
                color = TextDefault
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Sisa display",
                color = TextDefault
            )

            FlavorsInputRow(modifier = Modifier, values = display, onValueChange = onDisplayChanged)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Mentah",
                color = TextDefault
            )

            FlavorsInputRow(modifier = Modifier, values = raw, onValueChange = onRawChanged)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Sisa Bahan Baku",
                color = TextDefault
            )

            OutlinedTextField(
                value = oil,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) onOilChanged(filtered)
                },
                label = { Text(Ingredient.OIL.label) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Botol (liter)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = flour,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) onFlourChanged(filtered)
                },
                label = { Text(Ingredient.FLOUR.label) },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Plastik (500 gr)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lainnya Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "Lainnya",
                    color = TextDefault,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = onAddOther
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            others.forEachIndexed { index, resource ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = resource.name,
                                onValueChange = { onOtherNameChanged(index, it) },
                                label = { Text("Nama item") },
                                placeholder = { Text("contoh: Gas") },
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { onRemoveOther(index) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Item"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = resource.price,
                            onValueChange = {
                                val filtered = it.filter { char -> char.isDigit() }
                                if (filtered.length <= 8) onOtherPriceChanged(index, filtered)
                            },
                            label = { Text("Harga") },
                            supportingText = { Text("Rupiah") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            if (others.isEmpty()) {
                Text(
                    text = "Belum ada item tambahan. Tap 'Tambah' untuk menambahkan item seperti Gas, dll.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDefault.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(80.dp)) // Add space above button
        }
    }
}

private fun saveButtonClicked(
    reportId: String,
    oil: String,
    flour: String,
    display: Map<Flavor, Int>,
    raw: Map<Flavor, Int>,
    others: List<CheckoutViewModel.AdditionalResource>,
    viewModel: CheckoutViewModel
) {
    viewModel.onEvent(
        CheckoutViewModel.ViewEvent.OnButtonSaveClicked(
            reportId = reportId,
            oil = oil,
            flour = flour,
            display = display,
            raw = raw,
            others = others
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckoutScreenPreview() {
    CheckoutScreenContent(
        modifier = Modifier,
        todayDate = "Selasa, 22 September 2025",
        oil = "",
        flour = "",
        display = Flavor.entries.associateWith { 0 },
        raw = Flavor.entries.associateWith { 0 },
        others = listOf(
            CheckoutViewModel.AdditionalResource("Gas", "50000"),
            CheckoutViewModel.AdditionalResource("Listrik", "25000")
        ),
        onOilChanged = {},
        onFlourChanged = {},
        onDisplayChanged = { _, _ -> },
        onRawChanged = { _, _ -> },
        onAddOther = {},
        onRemoveOther = {},
        onOtherNameChanged = { _, _ -> },
        onOtherPriceChanged = { _, _ -> },
        onSaveClicked = {}
    )
}