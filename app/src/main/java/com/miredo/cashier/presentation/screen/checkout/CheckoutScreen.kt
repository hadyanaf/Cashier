package com.miredo.cashier.presentation.screen.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.miredo.cashier.presentation.components.AdditionalItemsSection
import com.miredo.cashier.presentation.components.CustomButton
import com.miredo.cashier.presentation.components.CustomTextField
import com.miredo.cashier.presentation.components.FlavorsInputRow
import com.miredo.cashier.presentation.components.GradientBackground
import com.miredo.cashier.presentation.components.RoundedTopAppBar
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
        Column {
            RoundedTopAppBar(
                title = "Checkout",
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
            Column(
                modifier = Modifier
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
                                color = BlueTertiary
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
                                color = TextDefault
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlavorsInputRow(
                                modifier = Modifier,
                                values = display,
                                onValueChange = onDisplayChanged
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                style = MaterialTheme.typography.titleSmall,
                                text = "Mentah",
                                color = TextDefault
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FlavorsInputRow(
                                modifier = Modifier,
                                values = raw,
                                onValueChange = onRawChanged
                            )
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

                // Additional Items Section with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_LONG) {
                    AdditionalItemsSection(
                        items = others,
                        onAddItem = onAddOther,
                        onRemoveItem = onRemoveOther,
                        onItemNameChanged = onOtherNameChanged,
                        onItemPriceChanged = onOtherPriceChanged
                    )
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