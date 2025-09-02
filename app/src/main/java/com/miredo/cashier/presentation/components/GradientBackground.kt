package com.miredo.cashier.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.BlueSecondary
import com.miredo.cashier.presentation.ui.theme.BlueTertiary

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BluePrimary.copy(alpha = 0.1f),
                        BlueSecondary.copy(alpha = 0.05f),
                        BlueTertiary.copy(alpha = 0.02f)
                    )
                )
            )
    ) {
        content()
    }
}