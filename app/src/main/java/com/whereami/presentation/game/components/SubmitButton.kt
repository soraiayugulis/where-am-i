package com.whereami.presentation.game.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whereami.R
import com.whereami.presentation.components.AppButton

@Composable
fun SubmitButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppButton(
        text = stringResource(R.string.game_submit_button),
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.padding(16.dp)
    )
}
