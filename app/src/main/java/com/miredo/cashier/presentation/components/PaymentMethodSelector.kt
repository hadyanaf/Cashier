package com.miredo.cashier.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun PaymentMethodSelector(
    modifier: Modifier,
    selectedMethod: PaymentType? = null,
    onMethodSelected: (PaymentType) -> Unit
) {
    val methods = PaymentType.entries.toTypedArray()

    Row(
        modifier = modifier.fillMaxWidth(), 
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        methods.forEach { method ->
            val isSelected = selectedMethod == method

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .then(
                        if (!isSelected) {
                            Modifier.border(
                                1.5.dp,
                                BluePrimary,
                                RoundedCornerShape(12.dp)
                            )
                        } else Modifier
                    ),
                onClick = { onMethodSelected(method) },
                shape = RoundedCornerShape(12.dp),
                colors = if (isSelected) {
                    ButtonDefaults.buttonColors(
                        containerColor = BluePrimary,
                        contentColor = White
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = BluePrimary
                    )
                },
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isSelected) 4.dp else 0.dp
                )
            ) {
                Text(
                    text = method.label,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
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