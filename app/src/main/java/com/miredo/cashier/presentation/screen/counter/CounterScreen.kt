package com.miredo.cashier.presentation.screen.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miredo.cashier.presentation.components.TextCounter

@Composable
fun CounterScreen(modifier: Modifier = Modifier, viewModel: CounterViewModel = hiltViewModel()) {
    val state by viewModel.state

    Scaffold(modifier = modifier, bottomBar = {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = { saveButtonClicked() }
        ) {
            Text("Simpan & Selesai")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            state.menuItems.forEach { item ->
                TextCounter(
                    title = item.title,
                    price = item.price,
                    count = item.count,
                    onIncrement = {
                        viewModel.onEvent(
                            CounterViewModel.ViewEvent.OnCountChanged(
                                item.title,
                                item.count + 1
                            )
                        )
                    },
                    onDecrement = {
                        viewModel.onEvent(
                            CounterViewModel.ViewEvent.OnCountChanged(
                                item.title,
                                (item.count - 1).coerceAtLeast(0)
                            )
                        )
                    },
                    onCountChange = {
                        viewModel.onEvent(
                            CounterViewModel.ViewEvent.OnCountChanged(
                                item.title,
                                it
                            )
                        )
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

private fun saveButtonClicked() {

}

@Preview
@Composable
private fun CounterScreenPreview() {
    CounterScreen(modifier = Modifier)
}