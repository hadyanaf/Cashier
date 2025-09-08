package com.miredo.cashier.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.GrayBorder
import com.miredo.cashier.presentation.ui.theme.GrayLight
import com.miredo.cashier.presentation.ui.theme.TextDefault
import com.miredo.cashier.presentation.ui.theme.White
import com.miredo.cashier.util.toRupiah

@Composable
fun TextCounter(
    title: String,
    price: Int,
    count: Int,
    onCountChange: (Int) -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    var inputValue by remember(count) { mutableStateOf(count.toString()) }
    val total = count * price

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side - Title and Price (flexible width)
        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextDefault,
                 maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formatPrice(price),
                style = MaterialTheme.typography.bodyMedium,
                color = BlueTertiary,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }

        // Center - Counter controls (fixed width)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            CounterButton(
                onClick = onDecrement,
                icon = Icons.Default.Remove,
                contentDescription = "Kurang",
                isPrimary = false,
                enabled = count > 0
            )

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
                    .width(45.dp)
                    .background(
                        GrayLight,
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.5.dp,
                        if (count > 0) BluePrimary.copy(alpha = 0.6f) else GrayBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 10.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = if (count > 0) BluePrimary else TextDefault
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            CounterButton(
                onClick = onIncrement,
                icon = Icons.Default.Add,
                contentDescription = "Tambah",
                isPrimary = true,
                enabled = true
            )
        }
    }
}

private fun formatPrice(price: Int): String {
    return "@${price.toRupiah()}"
}

@Composable
private fun CounterButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isPrimary: Boolean,
    enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_scale"
    )

    val backgroundColor = when {
        !enabled -> GrayLight
        isPrimary -> BluePrimary
        else -> White
    }

    val iconTint = when {
        !enabled -> TextDefault.copy(alpha = 0.4f)
        isPrimary -> White
        else -> BluePrimary
    }

    val borderColor = when {
        !enabled -> GrayBorder.copy(alpha = 0.5f)
        isPrimary -> BluePrimary
        else -> GrayBorder
    }

    IconButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .border(1.5.dp, borderColor, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextCounterPreview() {
    var count by remember { mutableIntStateOf(2) }
    val price = 4500

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextCounter(
            title = "Spicy",
            price = price,
            count = count,
            onIncrement = { count++ },
            onDecrement = { if (count > 0) count-- },
            onCountChange = { newCount -> count = newCount }
        )

        TextCounter(
            title = "Ori",
            price = 4000,
            count = 0,
            onIncrement = {},
            onDecrement = {},
            onCountChange = {}
        )
    }
}