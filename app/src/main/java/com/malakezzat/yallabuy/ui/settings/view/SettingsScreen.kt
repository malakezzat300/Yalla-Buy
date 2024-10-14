package com.malakezzat.yallabuy.ui.settings.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.sharedpref.CurrencyPreferences
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModel
import com.malakezzat.yallabuy.ui.shoppingcart.view.DeleteConfirmationDialog
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun SettingsScreen(navController: NavController,viewModel: SettingsViewModel/*,address: Address*/) {
    val addresses: List<String> = listOf()
    val context = LocalContext.current
    var showAddressDialog by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf(CurrencyPreferences.getInstance(context).getTargetCurrency()) }
    val exchangeRate by viewModel.conversionRate.collectAsState()
    val userAddressesState by viewModel.userAddresses.collectAsState()
    var userAddresses by remember { mutableStateOf(listOf<Address>()) }
    val userId by viewModel.userId.collectAsState()
    val deleteAddressState by viewModel.deleteAddressEvent.collectAsState("")

    LaunchedEffect(Unit) {
        viewModel.getUserId()
    }

    val messageState = viewModel.deleteAddressEvent.collectAsState(initial = "")

    LaunchedEffect(Unit) {
        viewModel.deleteAddressEvent.collect { message ->
            if(message.contains("422")){
                Toast.makeText(context, "Cannot Delete The Default Address", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when(exchangeRate){
        is ApiState.Error -> Log.i("currencyTest", "SettingsScreen: exchangeRate ${(exchangeRate as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
                CurrencyPreferences.getInstance(context).saveExchangeRate((exchangeRate as ApiState.Success).data?.conversion_rates)
        }
    }

    when(userAddressesState){
        is ApiState.Error -> Log.i("addressTest", "SettingsScreen: userAddressesState ${(userAddressesState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            userAddresses = (userAddressesState as ApiState.Success).data.addresses
            Log.i("addressTest", "SettingsScreen: $userAddresses")
        }
    }

    LaunchedEffect(Unit){
        viewModel.userId.value?.let { viewModel.getUserAddresses(it) }

    }

    val onAddNewAddress: () -> Unit = {
        showAddressDialog = true
    }
    val onDeleteAddress: (String) -> Unit = {
        //TODO delete address from api
    }
    val onCurrencyChange: (String) -> Unit = { currecny ->
        selectedCurrency = currecny
        CurrencyPreferences.getInstance(context).changeTargetCurrency(
            selectedCurrency!!
        )
        CurrencyPreferences.getInstance(context).getTargetCurrency()?.let { viewModel.getRate() }
    }

    androidx.compose.material3.Scaffold(
        topBar = { CustomTopBar(navController,"Settings", AppColors.Teal,{navController.navigateUp()}) },
        containerColor = Color.White,
        content = { paddingValues ->


            Column(modifier = Modifier.padding(16.dp).padding(paddingValues)) {

                /*Text(text = "Settings",
                    style = MaterialTheme.typography.headlineLarge,
                )*/

                Spacer(modifier = Modifier.height(8.dp))


                var expanded by remember { mutableStateOf(false) }
                val currencies = listOf("EGP", "USD", "EUR", "AED", "SAR")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Currency", color = AppColors.Teal,
                        style = MaterialTheme.typography.headlineSmall)
                    Row {
                        selectedCurrency?.let {
                            Text(text = it,
                                style = MaterialTheme.typography.headlineSmall)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "ArrowDropDown",
                            Modifier.size(30.dp)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            currencies.forEach { currency ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = currency,
                                            style = MaterialTheme.typography.headlineSmall)
                                    },
                                    onClick = {
                                        onCurrencyChange(currency)
                                        expanded = false
                                    })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "Address", color = AppColors.Teal,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(50.dp))

                    Button(
                        onClick = onAddNewAddress
                        ,
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Add new",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(userAddresses.size){
                        addressItem(navController,viewModel,userAddresses[it],userId)
                    }
                }



            }
        }
    )

    if (showAddressDialog) {
        AddressDialog(
            showDialog = showAddressDialog,
            onDismiss = {
                showAddressDialog = false
                        },
            onWriteAddress = {
                navController.navigate(Screen.AddressScreen.createRoute(""))
                showAddressDialog = false
            },
            onGetFromMap = {
                val latitude = 27.18039285293778
                val longitude = 31.186714348461493
                navController.navigate("mapScreen/$latitude/$longitude")
                showAddressDialog = false
            }
        )

    }
}


@Composable
fun addressItem(navController: NavController,viewModel: SettingsViewModel,address: Address,userId : Long?){
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate(Screen.AddressInfoScreen.createRoute(address.id))
            },
        elevation = CardDefaults.cardElevation(12.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${address.first_name} ${address.last_name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${address.phone}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.Teal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = "${address.address1}",
                    modifier = Modifier.align(Alignment.CenterStart),
                    style = MaterialTheme.typography.bodyLarge
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    if(address.default == true) {
                        Text(
                            text = "Default Address",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row {
                    IconButton(onClick = {
                        address.id?.let {
                            navController.navigate( Screen.AddressScreen.createRoute(it) )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
    if (showDialog) {
        DeleteConfirmationDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirmDelete = {
                showDialog = false
                userId?.let { address.id?.let { it1 ->
                    viewModel.deleteAddress(it,
                        it1
                    )
                } }
            }
        )

    }


}

@Composable
fun AddressDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onWriteAddress: () -> Unit,
    onGetFromMap: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Address From Map" ,color = AppColors.Teal)
            },
            text = {
                Text("Do you want to get address from Map?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onGetFromMap()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onWriteAddress()
                        onDismiss()
                    }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("No", color = Color.White)
                }
            }
        )
    }
}