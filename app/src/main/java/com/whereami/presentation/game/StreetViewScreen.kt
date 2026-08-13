package com.whereami.presentation.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import com.google.android.gms.maps.model.StreetViewPanoramaLocation
import com.whereami.domain.model.Location
import com.whereami.domain.session.GameSessionState
import com.whereami.presentation.components.AppButton
import com.whereami.presentation.error.ErrorScreen
import com.whereami.presentation.game.components.GameTimerBar

private const val PANORAMA_SEARCH_RADIUS_METERS = 5_000

@Composable
fun StreetViewScreen(
    viewModel: StreetViewViewModel = hiltViewModel(),
    onGuess: () -> Unit = {},
    onFinished: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val hasError by viewModel.hasError.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.resumeTimer()
                Lifecycle.Event.ON_PAUSE -> viewModel.pauseTimer()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (hasError) {
        ErrorScreen(
            message = "Could not load a Street View location. Please try again.",
            onHome = onHome
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val current = state) {
            is GameSessionState.Playing -> {
                StreetViewPanorama(
                    target = current.target,
                    onNoCoverage = viewModel::onNoCoverage
                )
                GameTimerBar(
                    remainingSeconds = current.remainingSeconds,
                    isWarning = current.isWarning,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                AppButton(
                    text = "Guess it!",
                    onClick = onGuess,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 80.dp)
                )
            }
            is GameSessionState.Finished -> LaunchedEffect(Unit) { onFinished() }
            GameSessionState.Idle -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun StreetViewPanorama(
    target: Location,
    onNoCoverage: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val position = LatLng(target.lat, target.lng)
    val currentOnNoCoverage by rememberUpdatedState(onNoCoverage)

    val panoramaView = remember {
        val options = StreetViewPanoramaOptions()
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

    LaunchedEffect(position) {
        panoramaView.getStreetViewPanoramaAsync { panorama ->
            panorama.setOnStreetViewPanoramaChangeListener { location: StreetViewPanoramaLocation? ->
                if (location?.position == null) currentOnNoCoverage()
            }
            panorama.setPosition(position, PANORAMA_SEARCH_RADIUS_METERS)
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
