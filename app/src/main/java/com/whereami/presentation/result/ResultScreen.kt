package com.whereami.presentation.result

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.whereami.presentation.components.AppButton
import com.whereami.presentation.theme.DarkBlue
import com.whereami.presentation.theme.EarthGreen
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.NunitoFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.SkyTop
import com.whereami.presentation.theme.White

@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel(),
    onHome: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val result = state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyTop.copy(alpha = 0.92f))
    ) {
        if (result == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = PinRed)
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (result.status == Status.COMPLETED) "ROUND COMPLETE!" else "TIME'S UP!",
                    style = MaterialTheme.typography.displaySmall,
                    color = White,
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                ResultMap(
                    target = result.target,
                    guess = result.guess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                val targetCountry = result.targetAddress?.country
                val guessCountry = result.guessAddress?.country
                val sameCountry = !targetCountry.isNullOrBlank() && targetCountry == guessCountry

                val targetValue = if (sameCountry) {
                    result.targetAddress?.format() ?: "Unknown location"
                } else {
                    targetCountry ?: "Unknown location"
                }

                val guessValue = if (sameCountry) {
                    result.guessAddress?.format() ?: "Unknown location"
                } else {
                    guessCountry ?: "Unknown location"
                }

                DetailRow(label = "Target", value = targetValue)
                if (result.status == Status.COMPLETED) {
                    Spacer(modifier = Modifier.height(14.dp))
                    DetailRow(label = "Your guess", value = guessValue)
                    if (result.distanceKm != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(label = "Distance", value = "%.1f km".format(result.distanceKm))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Score:",
                    style = MaterialTheme.typography.headlineSmall,
                    color = PinRed,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = result.score.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    color = EarthGreen,
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            AppButton(
                text = "Back to Home",
                onClick = onHome
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            color = DarkBlue,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
        Text(
            text = value,
            color = White,
            fontFamily = NunitoFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
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
