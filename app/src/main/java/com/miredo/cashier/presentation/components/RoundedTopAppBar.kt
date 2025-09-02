package com.miredo.cashier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.ui.theme.BlueTertiary
import com.miredo.cashier.presentation.ui.theme.White

@Composable
fun RoundedTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    cornerRadius: Int = 24
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .clip(
                RoundedCornerShape(
                    bottomStart = cornerRadius.dp,
                    bottomEnd = cornerRadius.dp
                )
            )
            .background(BlueTertiary)
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation Icon
            Box(
                modifier = Modifier.weight(0.1f),
                contentAlignment = Alignment.CenterStart
            ) {
                navigationIcon?.invoke()
            }
            
            // Title
            Box(
                modifier = Modifier.weight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            // Actions
            Box(
                modifier = Modifier.weight(0.1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                actions?.invoke()
            }
        }
    }
}