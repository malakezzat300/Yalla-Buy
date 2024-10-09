package com.malakezzat.yallabuy.ui.payment.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel

@Composable
fun CheckoutView(viewModel: PaymentViewModel,
                 navController: NavController,
                 orderId : String?
) {
    val availableAddresses = listOf("123 Main St", "456 Pine St", "789 Oak St")
    val paymentMethods = listOf("Credit Card","Cash on Delivery")

    CheckoutScreen(
        availableAddresses = availableAddresses,
        paymentMethods = paymentMethods,
        onAddressSelected = { address ->
            Log.d("Checkout", "Address Selected: $address")
        },
        onCouponApplied = { coupon ->
            Log.d("Checkout", "Coupon Applied: $coupon")
        },
        onPaymentMethodSelected = { method ->
            Log.d("Checkout", "Payment Method Selected: $method")
        },
        orderId = orderId,
        viewModel,
        navController,
    )
}

@Composable
fun CheckoutScreen(
    availableAddresses: List<String>,
    paymentMethods: List<String>,
    onAddressSelected: (String) -> Unit,
    onCouponApplied: (String) -> Unit,
    onPaymentMethodSelected: (String) -> Unit,
    orderId: String?,
    viewModel: PaymentViewModel,
    navController: NavController
) {
    var selectedAddress by remember { mutableStateOf("") }
    var couponCode by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("") }

//    val paymentKey by viewModel.paymentKey.collectAsState()

    Log.i("paymentTest", "CheckoutScreen: orderId $orderId")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Choose Address Dropdown
        Text("Choose Delivery Address", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedAddress,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableAddresses.forEach { address ->
                    DropdownMenuItem(
                        text = { Text(text = "list") },
                        onClick = {
                            selectedAddress = address
                            onAddressSelected(address)
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(text = if (selectedAddress.isEmpty()) "No address selected" else "Selected Address: $selectedAddress")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Enter Coupon Code", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = couponCode,
            onValueChange = { couponCode = it },
            label = { Text("Coupon Code") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )


        Button(
            enabled = couponCode.isNotBlank(),
            onClick = { onCouponApplied(couponCode) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Apply Coupon")
        }
        Spacer(modifier = Modifier.height(16.dp))


        Text("Choose Payment Method", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        paymentMethods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (selectedPaymentMethod == method),
                    onClick = {
                        selectedPaymentMethod = method
                        onPaymentMethodSelected(method)
                    }
                )
                Text(text = method)
                if(method == "Credit Card"){
                    Log.i("paymentTest", "Checkout: Credit Card")
//                    val paymentKeyRequest = FakeData.paymentKeyRequest.apply {
////                        this.order_id = orderId.toString()
//                    }
//                    viewModel.fetchPaymentKey(paymentKeyRequest)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


//        Log.i("paymentTest", "Checkout: paymentKey $paymentKey")
        Button(
            onClick = {
                if(selectedPaymentMethod == "Credit Card"){

                    //navController.navigate(Screen.PaymentScreen.route.replace("{paymentKey}", paymentKey ?: "default key"))
                } else {
                    //with cash
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Order")
        }
    }
}
