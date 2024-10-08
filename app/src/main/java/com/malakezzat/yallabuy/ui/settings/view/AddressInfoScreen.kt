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
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.ui.theme.AppColors


@Composable
fun AddressInfoScreen(navController: NavHostController, address: Address){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item{
            Text(
                text = "Address Info",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "First Name", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            address.first_name?.let { Text(text = it, fontSize = 18.sp) }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Last Name", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            address.last_name?.let { Text(text = it, fontSize = 18.sp) }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Phone Number", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            address.phone?.let { Text(text = it, fontSize = 18.sp) }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Address", fontSize = 18.sp,)
            Spacer(modifier = Modifier.height(2.dp))
            address.address1?.let { Text(text = it, fontSize = 18.sp,) }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "City", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            address.city?.let { Text(text = it, fontSize = 18.sp) }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Country", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            address.country?.let { Text(text = it, fontSize = 18.sp) }

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
                    text = "Edit",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}


