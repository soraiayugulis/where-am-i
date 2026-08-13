@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.whereami.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.whereami.R

private val fredokaWeights = listOf(
    FontWeight.Normal to 400,
    FontWeight.Medium to 500,
    FontWeight.SemiBold to 600,
    FontWeight.Bold to 700,
    FontWeight.ExtraBold to 800
)

private val nunitoWeights = listOf(
    FontWeight.Normal to 400,
    FontWeight.Medium to 500,
    FontWeight.SemiBold to 600,
    FontWeight.Bold to 700,
    FontWeight.ExtraBold to 800
)

val FredokaFontFamily = FontFamily(
    fredokaWeights.map { (weight, value) ->
        Font(
            resId = R.font.fredoka,
            weight = weight,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(value)
            )
        )
    }
)

val NunitoFontFamily = FontFamily(
    nunitoWeights.map { (weight, value) ->
        Font(
            resId = R.font.nunito,
            weight = weight,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(value)
            )
        )
    }
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 57.sp),
    displayMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 45.sp),
    displaySmall = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = NunitoFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp)
)
