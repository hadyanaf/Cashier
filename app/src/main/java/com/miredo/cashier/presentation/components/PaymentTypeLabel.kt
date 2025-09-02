package com.miredo.cashier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun PaymentTypeLabel(type: PaymentType?, modifier: Modifier) {
    val backgroundColor = when (type) {
        PaymentType.CASH -> MaterialTheme.colorScheme.secondary
        PaymentType.QRIS -> BluePrimary
        null -> MaterialTheme.colorScheme.outline
    }
    
    val textColor = when (type) {
        PaymentType.CASH -> MaterialTheme.colorScheme.onSecondary
        PaymentType.QRIS -> White
        null -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type?.label ?: "Unknown",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PaymentTypeLabelPreview() {
    PaymentTypeLabel(type = PaymentType.CASH, modifier = Modifier)
}