package com.malakezzat.yallabuy.ui.settings.view

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale


private var isZooming = false

@Composable
fun MapScreen(latitude: Double, longitude: Double, onMarkerClick: (String) -> Unit) {
    var selectedAddress by remember { mutableStateOf("") }

    Column {
        Column(
            modifier = Modifier.fillMaxHeight(0.8f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OsmMapView(latitude, longitude) { address ->
                selectedAddress = address

            }

        }
        Column(
            Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(selectedAddress)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    onMarkerClick(selectedAddress)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                enabled = selectedAddress.isNotEmpty()
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

        }
    }

}

@Composable
fun OsmMapView(
    latitude: Double,
    longitude: Double,
    onMarkerClick: (String) -> Unit
) {
    val context = LocalContext.current
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    // Creating a marker outside the AndroidView to ensure it's not recreated on every update
    val marker = remember { Marker(MapView(context)).apply {
        position = GeoPoint(latitude, longitude)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    }}

    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                setTileSource(TileSourceFactory.MAPNIK)
                this.setMultiTouchControls(true)
                controller.setZoom(5.0)
                val startPoint = GeoPoint(27.18039285293778, 31.186714348461493)
                controller.setCenter(startPoint)

                overlays.add(marker)
                invalidate()
            }
            mapView.setOnTouchListener { _, event ->
                if(!handleMapTouch(context,mapView,event).equals("Address not found")) {
                    Log.i("mapTest", "OsmMapView: ${handleMapTouch(context,mapView,event)}")

                    onMarkerClick(handleMapTouch(context, mapView, event))
                }
                false
            }
            mapView

        },
        update = { mapView ->
            mapView.controller.setCenter(GeoPoint(latitude, longitude))
        }
    )
}

private fun handleMapTouch(context : Context,mapView: MapView,event: MotionEvent): String {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            isZooming = false
        }
        MotionEvent.ACTION_MOVE -> {
            if (event.pointerCount > 1) {
                isZooming = true
            }
        }
        MotionEvent.ACTION_UP -> {
            if (!isZooming) {
                val projection = mapView.projection
                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint

                val latitude = geoPoint.latitude
                val longitude = geoPoint.longitude

                var address = addMarkerAtLocation(context,mapView,latitude, longitude)

                return address


            }
        }
    }
    return "Address not found"
}

private fun addMarkerAtLocation(context: Context,mapView : MapView,latitude: Double, longitude: Double) :String {
    val newMarker = Marker(mapView)
    newMarker.position = GeoPoint(latitude, longitude)
    newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    newMarker.title = "Marker at ($latitude, $longitude)"
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    mapView.overlays.clear()
    mapView.overlays.add(newMarker)
    mapView.invalidate()
    if(addresses != null && addresses.size > 0) {
        return addresses?.get(0)?.getAddressLine(0) ?: "Address not available"
    } else {
        return "Address not found"
    }
}
