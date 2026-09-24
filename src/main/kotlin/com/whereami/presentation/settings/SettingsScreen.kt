package com.whereami.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whereami.domain.model.LanguageOption
import com.whereami.presentation.components.LanguageOption

@Composable
fun SettingsScreen(
    selectedLanguage: LanguageOption,
    onLanguageSelected: (LanguageOption) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(LanguageOption.values()) { language ->
            LanguageOption(
                language = language,
                isSelected = selectedLanguage == language,
                onSelected = { onLanguageSelected(language) }
            )
        }
    }
}

@Composable
fun LanguageOption(
    language: LanguageOption,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = language.displayName,
            color = if (isSelected) Color.DarkBlue else Color.DarkBlue.copy(alpha = 0.15f),
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
            shadow = if (isSelected) Shadow(color = Color.White, offset = Offset(0f, 0f), blurRadius = 8f) else Shadow(color = Color.Transparent, offset = Offset(0f, 0f), blurRadius = 8f),
            modifier = Modifier.padding(16.dp)
        )
    }
}
