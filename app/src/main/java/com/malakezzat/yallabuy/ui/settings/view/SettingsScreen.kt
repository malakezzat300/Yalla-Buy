package com.malakezzat.yallabuy.ui.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.ui.Screen

@Composable
fun SettingsScreen(navController: NavController) {
    val addresses: List<String> = listOf()
    var selectedCurrency by remember { mutableStateOf("EGP") }
    val onAddNewAddress: () -> Unit = {
        navController.navigate(Screen.AddressScreen.createRoute(""))
    }
    val onDeleteAddress: (String) -> Unit = {
        //TODO delete address from api
    }
    val onCurrencyChange: (String) -> Unit = { currecny ->
        //TODO logic - SharedPreferences
        selectedCurrency = currecny
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Address",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(50.dp))

            Button(
                onClick = onAddNewAddress
                ,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
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

        addresses.forEach { address ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = address)
                Button(onClick = { onDeleteAddress(address) }) {
                    Text(text = "Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        val currencies = listOf("EGP", "USD", "EUR", "AED", "SAR")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Currency",
                style = MaterialTheme.typography.headlineSmall)
            Row {
                Text(text = selectedCurrency,
                    style = MaterialTheme.typography.headlineSmall)
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


    }
}