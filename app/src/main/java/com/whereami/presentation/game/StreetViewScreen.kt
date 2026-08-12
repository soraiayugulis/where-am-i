package com.whereami.presentation.game

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng
import com.whereami.domain.model.Location
import com.whereami.domain.session.GameSessionState

@Composable
fun StreetViewScreen(
    viewModel: StreetViewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val playing = state as? GameSessionState.Playing

    LaunchedEffect(Unit) {
        viewModel.startGame(1)
    }

    if (playing != null) {
        StreetViewPanoramaView(
            target = playing.target
        )
    }
}

@Composable
private fun StreetViewPanoramaView(
    target: Location
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val panoramaView = remember { StreetViewPanoramaView(context) }

    AndroidView(
        factory = {
            panoramaView.apply { onCreate(Bundle()) }
        },
        update = { _ ->
            panoramaView.getStreetViewPanoramaAsync(
                OnStreetViewPanoramaReadyCallback { panorama: StreetViewPanorama ->
                    panorama.setPosition(LatLng(target.lat, target.lng))
                    panorama.setUserNavigationEnabled(true)
                    panorama.setPanningGesturesEnabled(true)
                    panorama.setZoomGesturesEnabled(true)
                }
            )
        }
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> panoramaView.onResume()
                Lifecycle.Event.ON_PAUSE -> panoramaView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            panoramaView.onPause()
            panoramaView.onDestroy()
        }
    }
}
