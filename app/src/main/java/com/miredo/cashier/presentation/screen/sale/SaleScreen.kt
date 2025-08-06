package com.miredo.cashier.presentation.screen.sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.miredo.cashier.presentation.components.SaleItem

@Composable
fun SaleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
) {
    SaleScreenContent(modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreenContent(
    modifier: Modifier = Modifier,
    onButtonSaveClicked: () -> Unit = {},
    onButtonAddClicked: () -> Unit = {}
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = { Text("Rekap Penjualan") }
        )
    }, bottomBar = {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = { onButtonSaveClicked() },
        ) {
            Text(
                text = "Simpan & Lanjutkan"
            )
        }
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), onClick = { onButtonAddClicked() }) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Button"
                    )
                    Text(
                        text = "Tambah Penjualan"
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                SaleItem() { }
            }
        }
    }
}

@Preview
@Composable
private fun SaleScreenContentPreview() {
    SaleScreenContent()
}

