package com.whereami.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.whereami.R
import com.whereami.presentation.components.AppButton
import com.whereami.presentation.components.AppIconButton

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartGame: () -> Unit = {},
    onRanking: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isPortuguese = LocalContext.current.resources.configuration.locales[0].language == "pt"

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (isPortuguese) R.drawable.bg_home_pt else R.drawable.bg_home),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 64.dp)
            ) {
                AppButton(
                    text = stringResource(R.string.home_start_button),
                    onClick = onStartGame,
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    text = stringResource(R.string.home_ranking_button),
                    onClick = onRanking,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        AppIconButton(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings_button_content_description),
            onClick = onSettings,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 40.dp)
        )
    }
}
