package com.miredo.cashier.presentation.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AnimationConstants {
    const val DURATION_SHORT = 200
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500
    
    const val DELAY_SHORT = 50
    const val DELAY_MEDIUM = 100
    const val DELAY_LONG = 150
}

@Composable
fun SlideInAnimation(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                easing = FastOutSlowInEasing
            ),
            initialOffsetY = { it / 3 }
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            ),
            targetOffsetY = { -it / 3 }
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        content()
    }
}

@Composable
fun FadeInAnimation(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        content()
    }
}

@Composable
fun ExpandableAnimation(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = 0.8f,
                stiffness = 300f
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                easing = FastOutSlowInEasing
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        content()
    }
}

@Composable
fun ScaleInAnimation(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = 0.8f,
            stiffness = 400f
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = AnimationConstants.DURATION_MEDIUM,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )
    
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .scale(scale)
            .graphicsLayer(alpha = alpha)
    ) {
        if (visible) {
            content()
        }
    }
}

@Composable
fun SlideFromSideAnimation(
    visible: Boolean,
    fromLeft: Boolean = true,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                easing = FastOutSlowInEasing
            ),
            initialOffsetX = { if (fromLeft) -it else it }
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_MEDIUM,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            ),
            targetOffsetX = { if (fromLeft) -it else it }
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = AnimationConstants.DURATION_SHORT,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        content()
    }
}

@Composable
fun Modifier.animatedPadding(
    targetPadding: Dp,
    durationMillis: Int = AnimationConstants.DURATION_MEDIUM
): Modifier {
    val animatedPadding by animateDpAsState(
        targetValue = targetPadding,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        label = "padding"
    )
    return this.padding(animatedPadding)
}

@Composable
fun Modifier.animatedScale(
    targetScale: Float,
    durationMillis: Int = AnimationConstants.DURATION_SHORT
): Modifier {
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )
    return this.scale(animatedScale)
}