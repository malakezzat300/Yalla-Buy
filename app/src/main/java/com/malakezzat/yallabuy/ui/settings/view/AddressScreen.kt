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
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors


@Composable
fun AddressScreen(navController: NavHostController,viewModel: SettingsViewModel,address: String? = null){
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var addressState by remember { mutableStateOf(address ?: "") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var permissionGranted by remember { mutableStateOf(false) }
    var locationEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current



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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
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
                        onClick = {
                            val latitude = 27.18039285293778
                            val longitude = 31.186714348461493
                            navController.navigate("mapScreen/$latitude/$longitude")
                        },
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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
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
