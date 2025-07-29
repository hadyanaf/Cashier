package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import com.miredo.cashier.presentation.ui.theme.TextCaption
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
                color = TextDefault
            )

            Text(
                text = formatPrice(price),
                style = MaterialTheme.typography.bodySmall,
                color = TextCaption
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrement, modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus")
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
                    .widthIn(min = 36.dp, max = 36.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = onIncrement, modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Plus")
            }
        }

        Text(
            modifier = Modifier.width(90.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            color = TextDefault,
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