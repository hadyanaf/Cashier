package com.miredo.cashier.presentation.screen.checkin

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.data.enums.Ingredient
import com.miredo.cashier.presentation.animation.AnimationConstants
import com.miredo.cashier.presentation.animation.FadeInAnimation
import com.miredo.cashier.presentation.animation.SlideInAnimation
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
fun CheckInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CheckInViewModel = hiltViewModel()
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

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CheckInViewModel.ViewEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    CheckInScreenContent(
        modifier = modifier,
        todayDate = todayDate,
        oil = oil,
        flour = flour,
        display = display,
        raw = raw,
        onNavigateBack = { navController.popBackStack() },
        onOilChanged = { oil = it },
        onFlourChanged = { flour = it },
        onDisplayChanged = { flavor, count ->
            display = display.toMutableMap().apply { put(flavor, count) }
        },
        onRawChanged = { flavor, count -> raw = raw.toMutableMap().apply { put(flavor, count) } },
        onSaveClicked = {
            saveButtonClicked(
                oil = oil,
                flour = flour,
                display = display,
                raw = raw,
                viewModel = viewModel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreenContent(
    modifier: Modifier = Modifier,
    todayDate: String,
    oil: String,
    flour: String,
    display: Map<Flavor, Int>,
    raw: Map<Flavor, Int>,
    onNavigateBack: () -> Unit,
    onOilChanged: (String) -> Unit,
    onFlourChanged: (String) -> Unit,
    onDisplayChanged: (Flavor, Int) -> Unit,
    onRawChanged: (Flavor, Int) -> Unit,
    onSaveClicked: () -> Unit
) {

    GradientBackground {
        Column {
            RoundedTopAppBar(
                title = "Check In",
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
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
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
                                text = "Sisa display kemarin",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                text = "Bahan Baku",
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

                // Save Button with animation
                FadeInAnimation(visible = true, delayMillis = AnimationConstants.DELAY_LONG) {
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
    oil: String,
    flour: String,
    display: Map<Flavor, Int>,
    raw: Map<Flavor, Int>,
    viewModel: CheckInViewModel
) {
    viewModel.onEvent(
        CheckInViewModel.ViewEvent.OnButtonSaveClicked(
            oil = oil,
            flour = flour,
            display = display,
            raw = raw
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckInScreenPreview() {
    CheckInScreenContent(
        modifier = Modifier,
        todayDate = "Selasa, 22 September 2025",
        oil = "",
        flour = "",
        display = Flavor.entries.associateWith { 0 },
        raw = Flavor.entries.associateWith { 0 },
        onNavigateBack = {},
        onOilChanged = {},
        onFlourChanged = {},
        onDisplayChanged = { _, _ -> },
        onRawChanged = { _, _ -> },
        onSaveClicked = {}
    )
}