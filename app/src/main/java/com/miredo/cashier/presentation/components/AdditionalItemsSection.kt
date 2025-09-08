package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.screen.checkout.CheckoutViewModel
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.GrayLight
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun AdditionalItemsSection(
    items: List<CheckoutViewModel.AdditionalResource>,
    onAddItem: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onItemNameChanged: (Int, String) -> Unit,
    onItemPriceChanged: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pengeluaran Lainnya",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextDefault
                )

                IconButton(
                    onClick = onAddItem,
                    modifier = Modifier.size(40.dp)
                ) {
                    Card(
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = BluePrimary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Item",
                            tint = White,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GrayLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Belum ada item tambahan.\nTap + untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDefault.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        AdditionalItemCard(
                            item = item,
                            index = index,
                            onRemove = onRemoveItem,
                            onNameChanged = onItemNameChanged,
                            onPriceChanged = onItemPriceChanged
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdditionalItemCard(
    item: CheckoutViewModel.AdditionalResource,
    index: Int,
    onRemove: (Int) -> Unit,
    onNameChanged: (Int, String) -> Unit,
    onPriceChanged: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrayLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    value = item.name,
                    onValueChange = { onNameChanged(index, it) },
                    label = "Nama Item"
                )

                CustomTextField(
                    value = item.price,
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        if (filtered.length <= 8) onPriceChanged(index, filtered)
                    },
                    label = "Harga (Rupiah)"
                )
            }

            IconButton(
                onClick = { onRemove(index) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Item",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdditionalItemsSectionPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Empty state
        AdditionalItemsSection(
            items = emptyList(),
            onAddItem = {},
            onRemoveItem = {},
            onItemNameChanged = { _, _ -> },
            onItemPriceChanged = { _, _ -> }
        )

        // With items
        AdditionalItemsSection(
            items = listOf(
                CheckoutViewModel.AdditionalResource("Gas", "50000"),
                CheckoutViewModel.AdditionalResource("Listrik", "25000")
            ),
            onAddItem = {},
            onRemoveItem = {},
            onItemNameChanged = { _, _ -> },
            onItemPriceChanged = { _, _ -> }
        )
    }
}