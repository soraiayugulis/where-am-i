package com.whereami.presentation.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng
import com.whereami.domain.model.Location
import com.whereami.domain.session.GameSessionState
import com.whereami.presentation.game.components.GameTimerBar

@Composable
fun StreetViewScreen(
    viewModel: StreetViewViewModel = hiltViewModel(),
    onGuess: () -> Unit = {},
    onFinished: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame(System.currentTimeMillis().toInt())
    }

    LaunchedEffect(state) {
        if (state is GameSessionState.Finished) {
            onFinished()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val current = state) {
            is GameSessionState.Playing -> {
                StreetViewPanorama(target = current.target)
                GameTimerBar(
                    remainingSeconds = current.remainingSeconds,
                    isWarning = current.isWarning,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Button(
                    onClick = onGuess,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text("Guess")
                }
            }
            else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun StreetViewPanorama(target: Location) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val position = LatLng(target.lat, target.lng)

    val panoramaView = remember {
        val options = StreetViewPanoramaOptions()
            .position(position)
            .userNavigationEnabled(true)
            .panningGesturesEnabled(true)
            .zoomGesturesEnabled(true)
            .streetNamesEnabled(false)
        StreetViewPanoramaView(context, options).apply {
            onCreate(null)
            onStart()
            onResume()
        }
    }

    AndroidView(
        factory = { panoramaView },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> panoramaView.onStart()
                Lifecycle.Event.ON_RESUME -> panoramaView.onResume()
                Lifecycle.Event.ON_PAUSE -> panoramaView.onPause()
                Lifecycle.Event.ON_STOP -> panoramaView.onStop()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            panoramaView.onPause()
            panoramaView.onStop()
            panoramaView.onDestroy()
        }
    }
}
