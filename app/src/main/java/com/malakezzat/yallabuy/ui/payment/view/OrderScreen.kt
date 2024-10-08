package com.malakezzat.yallabuy.ui.payment.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.malakezzat.paymenttest2.model.FakeData
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors


@Composable
fun OrderScreen(viewModel: PaymentViewModel,
                navController: NavController) {
    var amount by remember { mutableLongStateOf(0L) }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val authToken by viewModel.authToken.collectAsState()
    val orderResponse by viewModel.orderResponse.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(50.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = amount.toString(), onValueChange = { amount = it.toLongOrNull() ?: 0L }, label = { Text("Amount (in cents)") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isLoading = true

            //make order with data
            authToken?.let { token ->
                val order = FakeData.orderRequest.apply { this.auth_token = token  }
                viewModel.createOrder(token,order)
            }
        }) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Pay Now")
            }
        }

        if (errorMessage != null) {
            Text(errorMessage ?: "", color = Color.Red)
            isLoading = false
        }

        if (orderResponse != null) {
            Text("Order Created: ${orderResponse?.id}", color = AppColors.MintGreen)
            if(isLoading) {
                navController.navigate(
                    Screen.CheckoutScreen.route.replace(
                        "{orderId}",
                        orderResponse?.id ?: ""
                    )
                )
            }
            isLoading = false
        }
    }
}