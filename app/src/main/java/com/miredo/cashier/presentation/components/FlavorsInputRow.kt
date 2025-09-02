package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.Flavor
import com.miredo.cashier.presentation.components.CustomTextField

@Composable
fun FlavorsInputRow(
    modifier: Modifier,
    values: Map<Flavor, Int?>,
    onValueChange: (Flavor, Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Flavor.entries.forEach { flavor ->
            CustomTextField(
                value = values[flavor]?.takeIf { it != 0 }?.toString().orEmpty(),
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() }
                    if (filtered.length <= 3) {
                        onValueChange(flavor, filtered.toIntOrNull() ?: 0)
                    }
                },
                label = flavor.label,
                modifier = Modifier.weight(1f)
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