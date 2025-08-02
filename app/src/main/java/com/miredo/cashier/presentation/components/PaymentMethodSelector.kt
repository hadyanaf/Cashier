package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.PaymentType

@Composable
fun PaymentMethodSelector(
    modifier: Modifier,
    selectedMethod: PaymentType? = null,
    onMethodSelected: (PaymentType) -> Unit
) {
    val methods = PaymentType.entries.toTypedArray()

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        methods.forEach { method ->
            val isSelected = selectedMethod == method

            val buttonColors = if (isSelected) {
                ButtonDefaults.buttonColors()
            } else {
                ButtonDefaults.outlinedButtonColors()
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onMethodSelected(method) },
                colors = buttonColors,
                border = if (!isSelected) ButtonDefaults.outlinedButtonBorder() else null
            ) {
                Text(method.label)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentMethodSelectorPreview() {
    PaymentMethodSelector(
        modifier = Modifier,
        selectedMethod = PaymentType.QRIS,
        onMethodSelected = {}
    )
}