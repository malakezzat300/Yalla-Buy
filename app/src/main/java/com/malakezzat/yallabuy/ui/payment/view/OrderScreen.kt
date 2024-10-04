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
import com.malakezzat.paymenttest2.model.FakeData
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel


@Composable
fun OrderScreen(viewModel: PaymentViewModel) {
    var amount by remember { mutableLongStateOf(0L) }
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val authToken by viewModel.authToken.collectAsState()
    val orderResponse by viewModel.orderResponse.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchToken("ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2T1RrNU1ETXlMQ0p1WVcxbElqb2lhVzVwZEdsaGJDSjkuS3ppcEdTb0hUdGM5UE1pNC1OUDJWc0dFdFVaUlFXTUxsWFlGZjJsVFJHcFZYTTQwTzh3MFZJdWVpYndiWm44Z3JZVVBXSU40aEhDa3ltVW1OSi0zelE=")
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
            Text("Order Created: ${orderResponse!!.id}", color = Color.Green)

            isLoading = false
        }
    }
}