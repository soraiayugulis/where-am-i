package com.whereami.presentation.result

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.whereami.domain.model.Location
import com.whereami.domain.model.Status

@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel(),
    onHome: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val result = state

    if (result == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ResultMap(
            target = result.target,
            guess = result.guess,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (result.status == Status.COMPLETED) "Round complete" else "Time's up",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Target: ${result.targetAddress?.format() ?: "Unknown location"}",
                modifier = Modifier.padding(top = 8.dp)
            )
            if (result.status == Status.COMPLETED) {
                Text("Your guess: ${result.guessAddress?.format() ?: "Unknown location"}")
                result.distanceKm?.let {
                    Text("Distance: ${"%.1f".format(it)} km")
                }
            }
            Text(
                text = "Score: ${result.score}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(
                onClick = onHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
private fun ResultMap(
    target: Location,
    guess: Location?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
            onStart()
            onResume()
        }
    }

    LaunchedEffect(target, guess) {
        mapView.getMapAsync { map: GoogleMap ->
            map.clear()
            val targetLatLng = LatLng(target.lat, target.lng)
            map.addMarker(
                MarkerOptions()
                    .position(targetLatLng)
                    .title("Target")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            if (guess == null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 5f))
                return@getMapAsync
            }
            val guessLatLng = LatLng(guess.lat, guess.lng)
            map.addMarker(
                MarkerOptions()
                    .position(guessLatLng)
                    .title("Your guess")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            map.addPolyline(
                PolylineOptions()
                    .add(targetLatLng, guessLatLng)
                    .color(android.graphics.Color.RED)
                    .width(6f)
            )
            val bounds = LatLngBounds.builder()
                .include(targetLatLng)
                .include(guessLatLng)
                .build()
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }
}
