package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.Flavor

@Composable
fun FlavorsInputRow(
    modifier: Modifier,
    values: Map<Flavor, Int>,
    onValueChange: (Flavor, Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Flavor.entries.forEach { flavor ->
            OutlinedTextField(
                value = values[flavor]?.toString() ?: "",
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) {
                        onValueChange(flavor, filtered.toIntOrNull() ?: 0)
                    }
                },
                label = { Text(flavor.label) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlavorsInputRowPreview() {
    FlavorsInputRow(
        modifier = Modifier,
        values = emptyMap(),
        onValueChange = { _, _ -> })
}