package com.whereami.presentation.game

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.whereami.presentation.theme.SkyBase

@Composable
fun GuessMapScreen(
    viewModel: GuessMapViewModel = hiltViewModel(),
    onSubmit: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val guess by viewModel.guess.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(state) {
        if (state is GameSessionState.Finished) {
            onSubmit()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.pauseTimer()
                Lifecycle.Event.ON_PAUSE -> viewModel.resumeTimer()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MapView(
            guess = guess,
            onMapClick = { viewModel.selectGuess(it) },
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SkyBase)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubmitButton(
                enabled = guess != null,
                onClick = { viewModel.confirmGuess() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MapView(
    guess: Location?,
    onMapClick: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnMapClick by rememberUpdatedState(onMapClick)
    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
            onStart()
            onResume()
        }
    }

    LaunchedEffect(Unit) {
        mapView.getMapAsync { loadedMap: GoogleMap ->
            googleMap = loadedMap
            loadedMap.uiSettings.isZoomControlsEnabled = true
            loadedMap.uiSettings.isCompassEnabled = true
            loadedMap.uiSettings.isScrollGesturesEnabled = true
            loadedMap.uiSettings.isZoomGesturesEnabled = true
            loadedMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 1f))

            loadedMap.setOnMapClickListener { latLng ->
                currentOnMapClick(Location(latLng.latitude, latLng.longitude))
            }
        }
    }

    LaunchedEffect(guess) {
        val map = googleMap ?: return@LaunchedEffect
        map.clear()
        guess?.let {
            val latLng = LatLng(it.lat, it.lng)
            map.addMarker(MarkerOptions().position(latLng))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f))
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
