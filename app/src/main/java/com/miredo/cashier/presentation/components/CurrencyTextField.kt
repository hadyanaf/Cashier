package com.miredo.cashier.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.miredo.cashier.presentation.ui.theme.BluePrimary
import com.miredo.cashier.presentation.ui.theme.GrayBorder
import com.miredo.cashier.presentation.ui.theme.GrayText
import com.miredo.cashier.util.CurrencyUtils

@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    prefix: String = "Rp "
) {
    var displayValue by remember(value) {
        mutableStateOf(
            if (value.isEmpty()) "" else CurrencyUtils.formatNumber(value)
        )
    }

    OutlinedTextField(
        value = displayValue,
        onValueChange = { newValue ->
            val numericOnly = newValue.replace("[^0-9]".toRegex(), "")
            displayValue = if (numericOnly.isNotEmpty()) {
                CurrencyUtils.formatNumber(numericOnly)
            } else {
                ""
            }
            onValueChange(numericOnly)
        },
        label = { Text(label) },
        prefix = { Text(prefix) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BluePrimary,
            unfocusedBorderColor = GrayBorder,
            focusedLabelColor = BluePrimary,
            unfocusedLabelColor = GrayText,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
        } else null,
        singleLine = true
    )
}