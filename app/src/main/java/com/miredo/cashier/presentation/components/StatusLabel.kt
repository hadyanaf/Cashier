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
import androidx.compose.ui.unit.dp
import com.miredo.cashier.data.enums.Status

@Composable
fun StatusLabel(status: Status, modifier: Modifier) {
    val bgColor = status.bgColor
    val textColor = status.textColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            text = status.label,
            color = textColor
        )
    }
}