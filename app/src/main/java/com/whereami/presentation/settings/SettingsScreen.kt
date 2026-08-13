package com.whereami.presentation.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.whereami.R
import com.whereami.domain.model.Language
import com.whereami.presentation.components.AppButton
import com.whereami.presentation.components.AppSmallButton
import com.whereami.presentation.theme.AccentYellow
import com.whereami.presentation.theme.DarkBlue
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.NunitoFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.SkyTop
import com.whereami.presentation.theme.White

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val language by viewModel.language.collectAsState()
    val context = LocalContext.current
    var showClearDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyTop.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
                color = PinRed,
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.settings_language_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkBlue,
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LanguageOption(
                        text = stringResource(R.string.settings_language_english),
                        selected = language == Language.EN,
                        onClick = {
                            viewModel.setLanguage(Language.EN)
                            (context as? Activity)?.recreate()
                        }
                    )
                    LanguageOption(
                        text = stringResource(R.string.settings_language_portuguese),
                        selected = language == Language.PT_BR,
                        onClick = {
                            viewModel.setLanguage(Language.PT_BR)
                            (context as? Activity)?.recreate()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkBlue.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings_clear_history_button),
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkBlue,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )
                    AppSmallButton(
                        text = stringResource(R.string.settings_clear_history_confirm),
                        onClick = { showClearDialog = true },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                Spacer(modifier = Modifier.width(8.dp))
                AppButton(
                    text = stringResource(R.string.settings_back_button),
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
    }

    if (showClearDialog) {
        ClearHistoryDialog(
            onConfirm = {
                viewModel.clearHistory()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }
}

@Composable
private fun LanguageOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (selected) DarkBlue.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        val textStyle = if (selected) {
        MaterialTheme.typography.titleMedium.copy(
            color = AccentYellow,
            fontWeight = FontWeight.ExtraBold,
            shadow = Shadow(
                color = White,
                offset = Offset(0f, 0f),
                blurRadius = 8f
            )
        )
    } else {
        MaterialTheme.typography.titleMedium.copy(
            color = DarkBlue,
            fontWeight = FontWeight.Normal
        )
    }

    Text(
        text = text,
        style = textStyle
    )
    }
}

@Composable
private fun ClearHistoryDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AccentYellow)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(White.copy(alpha = 0.75f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.settings_clear_history_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = DarkBlue,
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.settings_clear_history_message),
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkBlue,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.settings_clear_history_cancel),
                            style = MaterialTheme.typography.titleMedium,
                            color = DarkBlue,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.clickable { onDismiss() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AppSmallButton(
                            text = stringResource(R.string.settings_clear_history_confirm),
                            onClick = onConfirm,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
