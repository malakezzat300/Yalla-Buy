package com.malakezzat.yallabuy.ui.payment.view

import android.content.Context
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.LineItem

import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel
import com.malakezzat.yallabuy.ui.shoppingcart.view.calculateSubtotal

@Composable
fun CheckoutScreen(viewModel: PaymentViewModel,navController: NavController) {
    val userAddressesState by viewModel.userAddresses.collectAsState()
    val shoppingCartOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
    var userAddresses by remember { mutableStateOf(listOf<Address>()) }
    var addressesList by remember { mutableStateOf(listOf<String>()) }
    var defaultAddress by remember { mutableStateOf(Address()) }
    val shoppingCartOrder by remember { mutableStateOf(Address()) }
    var orderItems by remember { mutableStateOf( emptyList<LineItem>() ) }
    var itemsCount by remember { mutableIntStateOf(0) }
    var draftOrder by remember { mutableStateOf( DraftOrder() ) }

    val context = LocalContext.current
    CurrencyConverter.initialize(context)
    val userId by remember { mutableStateOf(context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE).getLong("USER_ID",0L)) }

    LaunchedEffect(Unit) {
        viewModel.getUserAddresses(userId)
        viewModel.getDraftOrders()
    }

    when(userAddressesState){
        is ApiState.Error -> Log.i("checkoutTest", "CheckoutScreen: userAddressesState ${(userAddressesState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            userAddresses = (userAddressesState as ApiState.Success).data.addresses
            getDefaultAddress(userAddresses).let {
                if (it != null) {
                    defaultAddress = it
                }
            }
            addressesList = getAddressesList(userAddresses)
        }
    }

    when(shoppingCartOrderState){
        is ApiState.Error ->{ Log.i("checkoutTest", "CheckoutScreen: draftOrder ${(shoppingCartOrder as ApiState.Error).message}") }
        ApiState.Loading -> {}
        is ApiState.Success -> {
            orderItems = (shoppingCartOrderState as ApiState.Success).data.line_items
            draftOrder = (shoppingCartOrderState as ApiState.Success).data
        }
    }

    LaunchedEffect(orderItems){
        itemsCount = orderItems.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { /* Navigate back */ }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Items (${itemsCount})")
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow Right"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Shipping Address",style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal))
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            if(defaultAddress.name?.isNotBlank() == true){
                InfoRow(label = "Full Name", value = "${defaultAddress.name}")
            }
            InfoRow(label = "Mobile Number", value = "${defaultAddress.phone}")
            InfoRow(label = "Country", value = "${defaultAddress.country}")
            InfoRow(label = "City", value = "${defaultAddress.city}")
            InfoRow(label = "Address", value = "${defaultAddress.address1}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Order Info", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal))
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            CurrencyConverter.changeCurrency(draftOrder.subtotal_price.toDouble())
                ?.let { InfoRow(label = "Subtotal", value = it) }
            CurrencyConverter.changeCurrency(draftOrder.total_tax.toDouble())
                ?.let { InfoRow(label = "Tax", value = it) }
            CurrencyConverter.changeCurrency(draftOrder.total_price.toDouble())
                ?.let { InfoRow(label = "Total", value = it, isBold = true) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Place order action */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Place Order")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isBold: Boolean = false)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            else MaterialTheme.typography.bodyMedium
        )
    }
}