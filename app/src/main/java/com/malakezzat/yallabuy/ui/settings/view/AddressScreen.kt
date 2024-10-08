package com.malakezzat.yallabuy.ui.settings.view

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.text.TextUtils.replace
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import java.util.Locale
import android.content.Context
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
import java.io.File
import java.io.IOException
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.CustomerId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavHostController,viewModel: SettingsViewModel,address: String? = null){
    var addressId by remember { mutableStateOf(0L) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var permissionGranted by remember { mutableStateOf(false) }
    var locationEnabled by remember { mutableStateOf(false) }
    var expandedCity by remember { mutableStateOf(false) }
    var expandedCountry by remember { mutableStateOf(false) }
    var cities by remember { mutableStateOf(emptyList<String>()) }
    val userId by viewModel.userId.collectAsState()
    val addNewAddressState by viewModel.customerAddress.collectAsState()
    val addressDetails by viewModel.addressDetails.collectAsState()
    var addressState by remember { mutableStateOf(address ?: "") }
    var saveButton by remember { mutableStateOf("Save") }
    var screenTitle by remember { mutableStateOf("New Address") }

    val sharedPreferences = LocalContext.current.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
    LaunchedEffect(Unit) {
        val isAddressId = address?.let { isAddressId(it) }
        if(isAddressId == true){
            addressState = ""
            viewModel.getAddressDetails(sharedPreferences.getLong("USER_ID", 0L),address.toLong())
            saveButton = "Update"
            screenTitle = "Edit Address"
        } else {
            if (address != null) {
                addressState = address
            }
        }
    }

    when(addressDetails){
        is ApiState.Error -> Log.i("addressTest", "Error: ${(addressDetails as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            LaunchedEffect(Unit) {
                addressId = (addressDetails as ApiState.Success).data?.customer_address?.id!!
                firstName = (addressDetails as ApiState.Success).data?.customer_address?.first_name.toString()
                lastName = (addressDetails as ApiState.Success).data?.customer_address?.last_name.toString()
                phoneNumber = (addressDetails as ApiState.Success).data?.customer_address?.phone.toString()
                addressState= (addressDetails as ApiState.Success).data?.customer_address?.address1.toString()
                city = (addressDetails as ApiState.Success).data?.customer_address?.city.toString()
                country = (addressDetails as ApiState.Success).data?.customer_address?.country.toString()
            }
        }
    }

    val context = LocalContext.current

    val countries = getCountries(context)

    LaunchedEffect(Unit) {
        viewModel.getUserId()
    }

    LaunchedEffect(country) {
        if (country.isNotEmpty()) {
            cities = getCitiesFromCountries(context, country)
        }
    }

    when(addNewAddressState){
        is ApiState.Error -> Toast.makeText(context, "Error: ${(addNewAddressState as ApiState.Error).message}", Toast.LENGTH_SHORT).show()
        ApiState.Loading -> {}
        is ApiState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Address has been added", Toast.LENGTH_SHORT).show()
                if(address?.let { isAddressId(it) } == true){
                    navController.navigateUp()
                } else {
                    navController.navigateUp()
                    navController.navigateUp()
                }
            }
        }
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            if (isLocationEnabled(context)) {
                getUserLocation(context, fusedLocationClient) { loc ->
                    addressState = loc
                }
            } else {
                locationEnabled = false
            }
        } else {
            addressState = "Permission denied"
        }
    }




    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item{
            Text(
                text = "New Address",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Phone Number ")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                        append("*")
                    }
                },
                fontSize = 18.sp
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    val regex = Regex("^0(1[0125])?\\d{0,8}$")
                    if (regex.matches(input) || input.isEmpty()) {
                        phoneNumber = input
                    } else {
                        Toast.makeText(context, "Enter a vaild number", Toast.LENGTH_SHORT).show()
                    }
                },
                label = { Text(text = "Phone Number") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(top = 2.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Address ")
                        withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                            append("*")
                        }
                    },
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
//                Text(text = "Address",
//                    fontSize = 18.sp,
//                    modifier = Modifier.align(Alignment.CenterVertically)
//                )
                Row {
//                    Button(
//                        onClick = {
//
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
//                        shape = RoundedCornerShape(10.dp)
//                    ) {
//                        Text(
//                            text = "Map",
//                            color = Color.White,
//                            fontSize = 16.sp
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                getUserLocation(context,fusedLocationClient) { loc ->
                                    addressState = loc
                                }
                            } else {
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
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
                value = addressState,
                onValueChange = {input -> addressState = input },
                label = { Text(text = "Address") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(top = 2.dp),
                colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Country ")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                        append("*")
                    }
                },
                fontSize = 18.sp,
            )
            ExposedDropdownMenuBox(
                expanded = expandedCountry,
                onExpandedChange = { expandedCountry = !expandedCountry }
            ) {
                TextField(
                    value = country,
                    onValueChange = { /* No change here since it's readOnly */ },
                    readOnly = true,
                    label = { Text(text = "Country") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Green
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCountry,
                    onDismissRequest = { expandedCountry = false }
                ) {
                    countries.forEach { countryOption ->
                        DropdownMenuItem(onClick = {
                            country = countryOption
                            expandedCountry = false
                        }) {
                            Text(text = countryOption)
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("City ")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)) {
                        append("*")
                    }
                },
                fontSize = 18.sp,
            )
            ExposedDropdownMenuBox(
                expanded = expandedCity,
                onExpandedChange = { if (cities.isEmpty()) {
                    Toast.makeText(context, "Choose a Country First", Toast.LENGTH_SHORT).show()
                } else {
                    expandedCity = !expandedCity
                } }
            ) {
                TextField(
                    value = city,
                    onValueChange = { /* No change here since it's readOnly */ },
                    readOnly = true,
                    label = { Text(text = "City") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        unfocusedIndicatorColor = Color.Green
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCity,
                    onDismissRequest = { expandedCity = false }
                ) {
                    cities.forEach { cityOption ->
                        DropdownMenuItem(onClick = {
                            city = cityOption
                            expandedCity = false
                        }) {
                            Text(text = cityOption)
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "First Name", fontSize = 18.sp)
            OutlinedTextField(
                value = firstName,
                onValueChange = { input ->
                    if (input.all { it.isLetter()  }) {
                        firstName = input
                    }
                },
                label = { Text(text = "First Name") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(top = 2.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Last Name", fontSize = 18.sp)
            OutlinedTextField(
                value = lastName,
                onValueChange = { input ->
                    if (input.all { it.isLetter() }) {
                        lastName = input
                    }
                },
                label = { Text(text = "Last Name") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(top = 2.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if(phoneNumber.isNotBlank()
                        && addressState.isNotBlank() && city.isNotBlank() && country.isNotBlank()) {
                        userId?.let {
                            val address1 = Address(
                                customer_id = it,
                                first_name = firstName,
                                last_name = lastName,
                                phone = phoneNumber,
                                address1 = addressState,
                                city = city,
                                country = country
                            )
                            if (address?.let { isAddressId(it) } == true) {
                                viewModel.updateUserAddress(it, addressId, AddressRequest(address1))
                            } else {
                                viewModel.addNewAddress(it, AddressRequest(address1))
                            }
                        }

                    } else {
                        Toast.makeText(context,"Please fill all fields" ,Toast.LENGTH_SHORT).show()
                    }


                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = saveButton,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
    if (!locationEnabled && permissionGranted) {
        LocationPrompt()
    }
}

fun getUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient,  onLocationReceived: (String) -> Unit) {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener { location: Location? ->
            if (location == null)
                onLocationReceived("Cannot get location.")
            else {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses?.get(0)?.getAddressLine(0) ?: "Address not available"

                onLocationReceived(address)
            }

        }

}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

@Composable
fun EnableLocationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Enable Location") },
        text = { Text("Please enable location services to use this feature.") },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("Settings")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun LocationPrompt() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (!isLocationEnabled(context)) {
        showDialog = true
    }

    if (showDialog) {
        EnableLocationDialog(
            onConfirm = {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

fun getCitiesFromCountries(context: Context, countryName: String): List<String> {
    val json: String
    try {
        val inputStream = context.assets.open("cities_countries.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return emptyList()
    }

    val type = object : TypeToken<Map<String, List<String>>>() {}.type
    val countriesWithCities: Map<String, List<String>> = Gson().fromJson(json, type)

    return countriesWithCities[countryName] ?: emptyList()
}

fun getCountries(context: Context): List<String> {
    val json: String
    try {
        val inputStream = context.assets.open("cities_countries.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return emptyList()
    }

    val type = object : TypeToken<Map<String, List<String>>>() {}.type
    val countriesWithCities: Map<String, List<String>> = Gson().fromJson(json, type)

    return countriesWithCities.keys.toList()
}

fun isAddressId(input: String): Boolean {
    return input.toLongOrNull() != null
}