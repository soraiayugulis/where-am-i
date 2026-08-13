package com.whereami.presentation.game.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.whereami.presentation.components.AppButton

@Composable
fun SubmitButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppButton(
        text = "I know where I am!",
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.padding(16.dp)
    )
}
