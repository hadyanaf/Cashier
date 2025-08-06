package com.miredo.cashier.presentation.screen.expense

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ExpenseScreen(modifier: Modifier = Modifier, viewModel: ExpenseViewModel = hiltViewModel()) {
    ExpenseScreenContent(modifier = modifier)
}

@Composable
fun ExpenseScreenContent(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun ExpenseScreenContentPreview() {
    ExpenseScreenContent()
}