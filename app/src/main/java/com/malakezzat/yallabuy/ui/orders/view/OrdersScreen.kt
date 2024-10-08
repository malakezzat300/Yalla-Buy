package com.malakezzat.yallabuy.ui.orders.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModel

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    navController: NavController
){

    val customerData by viewModel.customerDataByEmail.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getUserByEmail()
    }

}