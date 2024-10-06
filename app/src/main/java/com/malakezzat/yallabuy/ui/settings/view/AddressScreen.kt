package com.malakezzat.yallabuy.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun AddressScreen(){
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "New Address",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "First Name", fontSize = 18.sp)
        OutlinedTextField(
            value = firstName,
            onValueChange = { input -> firstName = input },
            label = { Text(text = "First Name") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Last Name", fontSize = 18.sp)
        OutlinedTextField(
            value = lastName,
            onValueChange = { input -> lastName = input },
            label = { Text(text = "Last Name") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Phone Number", fontSize = 18.sp)
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { input -> phoneNumber = input },
            label = { Text(text = "Phone Number") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Address",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Row {
                Button(
                    onClick = { /* Action for button click */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Map",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Action for button click */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "GPS",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
        OutlinedTextField(
            value = address,
            onValueChange = {input -> address = input },
            label = { Text(text = "Address") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "City", fontSize = 18.sp)
        OutlinedTextField(
            value = city,
            onValueChange = { input -> city = input },
            label = { Text(text = "City") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Country", fontSize = 18.sp)
        OutlinedTextField(
            value = country,
            onValueChange = { input -> country = input },
            label = { Text(text = "Country") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 2.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = { /* Action for button click */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 16.sp
            )
        }

    }
}
