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
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            style = MaterialTheme.typography.labelSmall,
            text = status.label,
            color = textColor,
            modifier = modifier
        )
    }
}