package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.PaymentType
import com.miredo.cashier.presentation.ui.theme.TextCaption
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.util.toRupiah

@Composable
fun SaleItem(modifier: Modifier, totalPrice: Int, items: Map<String, Int>, paymentType: PaymentType, onClick: () -> Unit) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = totalPrice.toRupiah(),
                style = MaterialTheme.typography.titleMedium,
                color = TextDefault,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(end = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                val itemsText = items.entries.joinToString(" | ") { "${it.key}: ${it.value}" }

                Text(
                    text = itemsText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextCaption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                PaymentTypeLabel(type = paymentType, modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaleItemPreview() {
    SaleItem(
        totalPrice = 25000,
        items = mapOf("O" to 1, "S" to 1, "C" to 3),
        paymentType = PaymentType.QRIS,
        modifier = Modifier,
        onClick = {})
}