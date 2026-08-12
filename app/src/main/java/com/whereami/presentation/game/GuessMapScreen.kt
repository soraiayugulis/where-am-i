package com.whereami.presentation.game

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.whereami.domain.model.Location
import com.whereami.domain.session.GameSessionState
import com.whereami.presentation.game.components.SubmitButton

@Composable
fun GuessMapScreen(
    viewModel: GuessMapViewModel = hiltViewModel(),
    onSubmit: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val guess by viewModel.guess.collectAsState()

    LaunchedEffect(state) {
        if (state is GameSessionState.Finished) {
            onSubmit()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MapView(
            initial = guess,
            onMapClick = { viewModel.selectGuess(it) },
            modifier = Modifier.weight(1f)
        )
        SubmitButton(
            enabled = guess != null,
            onClick = { viewModel.confirmGuess() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MapView(
    initial: Location?,
    onMapClick: (Location) -> Unit,
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

    AndroidView(
        factory = { mapView },
        update = { _ ->
            mapView.getMapAsync { googleMap: GoogleMap ->
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.uiSettings.isCompassEnabled = true

                val start = initial?.let { LatLng(it.lat, it.lng) } ?: LatLng(0.0, 0.0)
                val zoom = if (initial != null) 4f else 1f
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoom))

                googleMap.setOnMapClickListener { latLng ->
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(latLng))
                    onMapClick(Location(latLng.latitude, latLng.longitude))
                }
            }
        },
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
