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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whereami.presentation.theme.AccentYellow
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.White

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = AccentYellow
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = contentColor,
            disabledContainerColor = White,
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        ),
        border = BorderStroke(4.dp, PinRed),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.6f),
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
