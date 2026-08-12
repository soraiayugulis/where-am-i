package com.whereami.presentation.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameTimerBar(
    remainingSeconds: Int,
    isWarning: Boolean,
    modifier: Modifier = Modifier
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    Text(
        text = "%d:%02d".format(minutes, seconds),
        style = MaterialTheme.typography.headlineMedium,
        color = if (isWarning) Color.Red else Color.White,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(vertical = 12.dp)
    )
}
