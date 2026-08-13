package com.whereami.presentation.game.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.whereami.presentation.theme.AccentYellow
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.White

@Composable
fun GameTimerBar(
    remainingSeconds: Int,
    isWarning: Boolean,
    modifier: Modifier = Modifier
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    val pulse = if (isWarning) {
        val infiniteTransition = rememberInfiniteTransition()
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        ).value
    } else {
        1f
    }

    val textColor = if (isWarning) PinRed else AccentYellow
    val backgroundColor = White
    val borderColor = PinRed

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(top = 16.dp)
            .wrapContentSize()
            .scale(pulse)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(3.dp, borderColor),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Text(
            text = "%d:%02d".format(minutes, seconds),
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            textAlign = TextAlign.Center,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
