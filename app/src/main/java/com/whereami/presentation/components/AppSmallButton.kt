package com.whereami.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whereami.presentation.theme.AccentYellow
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.White

@Composable
fun AppSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = AccentYellow,
            disabledContainerColor = White,
            disabledContentColor = AccentYellow.copy(alpha = 0.6f)
        ),
        border = BorderStroke(3.dp, PinRed),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        modifier = modifier.height(40.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) AccentYellow else AccentYellow.copy(alpha = 0.6f),
            fontFamily = FredokaFontFamily,
            fontWeight = fontWeight
        )
    }
}
