package com.miredo.cashier.data.enums

import androidx.compose.ui.graphics.Color
import com.miredo.cashier.presentation.ui.theme.BgCheckIn
import com.miredo.cashier.presentation.ui.theme.BgCheckOut
import com.miredo.cashier.presentation.ui.theme.BgInProgress
import com.miredo.cashier.presentation.ui.theme.TextCheckIn
import com.miredo.cashier.presentation.ui.theme.TextCheckOut
import com.miredo.cashier.presentation.ui.theme.TextInProgress

enum class Status(val label: String, val bgColor: Color, val textColor: Color) {
    CHECKED_IN(
        label = "Check-In",
        bgColor = BgCheckIn,
        textColor = TextCheckIn
    ),
    IN_PROGRESS(
        label = "Sedang Berlangsung",
        bgColor = BgInProgress,
        textColor = TextInProgress
    ),
    CHECKED_OUT(
        label = "Check-Out",
        bgColor = BgCheckOut,
        textColor = TextCheckOut
    );
}