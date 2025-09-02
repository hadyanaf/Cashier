package com.miredo.cashier.presentation.screen.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.Ingredient
import com.miredo.cashier.data.model.Screen
import com.miredo.cashier.presentation.animation.AnimationConstants
import com.miredo.cashier.presentation.animation.FadeInAnimation
import com.miredo.cashier.presentation.animation.SlideInAnimation
import com.miredo.cashier.presentation.components.CustomButton
import com.miredo.cashier.presentation.components.CustomTextField
import com.miredo.cashier.presentation.components.FlavorsInputRow
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.presentation.ui.theme.White
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
        onNavigateBack = { navController.popBackStack() },
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
    onNavigateBack: () -> Unit,
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

    GradientBackground {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Checkout",
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
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date Card with animation
                SlideInAnimation(visible = true) {
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
                                style = MaterialTheme.typography.titleMedium,
                                text = "Hari/Tanggal",
                                color = TextDefault
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                style = MaterialTheme.typography.bodyLarge,
                                text = todayDate,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Stock Card with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_SHORT) {
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
                                style = MaterialTheme.typography.titleMedium,
                                text = "Stok Tahu",
                                color = TextDefault
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                style = MaterialTheme.typography.titleSmall,
                                text = "Sisa display",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlavorsInputRow(modifier = Modifier, values = display, onValueChange = onDisplayChanged)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                style = MaterialTheme.typography.titleSmall,
                                text = "Mentah",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlavorsInputRow(modifier = Modifier, values = raw, onValueChange = onRawChanged)
                        }
                    }
                }

                // Raw Materials Card with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_MEDIUM) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = "Sisa Bahan Baku",
                                color = TextDefault
                            )

                            CustomTextField(
                                value = oil,
                                onValueChange = {
                                    val filtered = it.filter { char -> char.isDigit() }
                                    if (filtered.length <= 3) onOilChanged(filtered)
                                },
                                label = "${Ingredient.OIL.label} (Botol)"
                            )

                            CustomTextField(
                                value = flour,
                                onValueChange = {
                                    val filtered = it.filter { char -> char.isDigit() }
                                    if (filtered.length <= 3) onFlourChanged(filtered)
                                },
                                label = "${Ingredient.FLOUR.label} (Plastik 500gr)"
                            )
                        }
                    }
                }

                // Additional Items Card with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_LONG) {
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
                                    style = MaterialTheme.typography.titleMedium,
                                    text = "Lainnya",
                                    color = TextDefault
                                )

                                OutlinedButton(
                                    onClick = onAddOther
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah Item"
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tambah")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (others.isEmpty()) {
                                Text(
                                    text = "Belum ada item tambahan. Tap 'Tambah' untuk menambahkan item seperti Gas, dll.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                others.forEachIndexed { index, resource ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                CustomTextField(
                                                    value = resource.name,
                                                    onValueChange = { onOtherNameChanged(index, it) },
                                                    label = "Nama Item",
                                                    modifier = Modifier.weight(1f)
                                                )

                                                IconButton(
                                                    onClick = { onRemoveOther(index) }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Hapus Item",
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            CustomTextField(
                                                value = resource.price,
                                                onValueChange = {
                                                    val filtered = it.filter { char -> char.isDigit() }
                                                    if (filtered.length <= 8) onOtherPriceChanged(index, filtered)
                                                },
                                                label = "Harga (Rupiah)"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Save Button with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_LONG + 100) {
                    CustomButton(
                        text = "Simpan",
                        onClick = onSaveClicked,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
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
        onNavigateBack = {},
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