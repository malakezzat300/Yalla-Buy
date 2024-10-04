package com.malakezzat.yallabuy.ui.payment.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun PaymentScreen(onPaymentSuccess: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Payment Details", style = MaterialTheme.typography.headlineLarge)

        // Input fields for card information
        OutlinedTextField(
            value = "", // handle state
            onValueChange = { /* handle value change */ },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to initiate payment
        Button(onClick = {
            // call your Paymob API
        }) {
            Text("Pay Now")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    YallaBuyTheme {
        PaymentScreen(){
            Log.i("paymentTest", "GreetingPreview: done")
        }
    }
}