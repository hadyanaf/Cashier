package com.miredo.cashier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.PaymentType

@Composable
fun PaymentTypeLabel(type: PaymentType?, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            style = MaterialTheme.typography.labelSmall,
            text = type?.label.orEmpty(),
            color = Color.White,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PaymentTypeLabelPreview() {
    PaymentTypeLabel(type = PaymentType.CASH, modifier = Modifier)
}