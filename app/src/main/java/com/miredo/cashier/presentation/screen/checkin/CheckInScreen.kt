package com.miredo.cashier.presentation.screen.checkin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.components.FlavorsInputRow
import com.miredo.cashier.presentation.ui.theme.TextDefault
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(modifier: Modifier = Modifier) {
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Check In") }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onClick = { saveButtonClicked() }
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
                text = "Sisa display kemarin",
                color = TextDefault
            )

            FlavorsInputRow(modifier = Modifier, list = listOf("Ori", "Spicy", "Chicken"))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                style = MaterialTheme.typography.titleSmall,
                text = "Mentah",
                color = TextDefault
            )

            FlavorsInputRow(modifier = Modifier, list = listOf("Ori", "Spicy", "Chicken"))

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Bahan Baku",
                color = TextDefault
            )

            OutlinedTextField(
                value = oil,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) oil = filtered
                },
                label = { Text("Minyak") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Botol (liter)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = flour,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) flour = filtered
                },
                label = { Text("Tepung") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = { Text("Plastik (500 gr)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(80.dp)) // Add space above button
        }
    }
}

private fun saveButtonClicked() {

}

@Preview(showBackground = true)
@Composable
private fun CheckInScreenPreview() {
    CheckInScreen(Modifier)
}