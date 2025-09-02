package com.miredo.cashier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.util.toRupiah

@Composable
fun TextCounter(
    title: String,
    price: Int,
    count: Int,
    onCountChange: (Int) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier
) {
    var inputValue by remember(count) { mutableStateOf(count.toString()) }
    val total = count * price

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDefault,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = formatPrice(price),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrement, 
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .border(
                        1.dp, 
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Remove, 
                    contentDescription = "Kurang",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            BasicTextField(
                value = inputValue,
                onValueChange = {
                    val sanitized = it.filter { char -> char.isDigit() }.take(3)
                    inputValue = sanitized
                    sanitized.toIntOrNull()?.let { parsed ->
                        onCountChange(parsed)
                    } ?: onCountChange(0)
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = 40.dp, max = 40.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = onIncrement, 
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add, 
                    contentDescription = "Tambah",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text(
            modifier = Modifier.width(90.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            text = total.toRupiah(),
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatPrice(price: Int): String {
    return "(@${price.toRupiah()})"
}

@Preview(showBackground = true)
@Composable
fun TextCounterPreview() {
    var count by remember { mutableIntStateOf(2) }
    var price by remember { mutableIntStateOf(3000) }

    TextCounter(
        title = "Spicy",
        price = price, // i want it user input string 3000 and convert to this type of string
        count = count,
        onIncrement = { count++ },
        onDecrement = { if (count > 0) count-- },
        onCountChange = { newCount -> count = newCount },
        modifier = Modifier
    )
}