package com.whereami.presentation.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.whereami.presentation.theme.AccentYellow
import com.whereami.presentation.theme.DarkBlue
import com.whereami.presentation.theme.FredokaFontFamily
import com.whereami.presentation.theme.NunitoFontFamily
import com.whereami.presentation.theme.PinRed
import com.whereami.presentation.theme.SkyTop
import com.whereami.R
import com.whereami.domain.model.Status
import com.whereami.presentation.theme.White
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RankingScreen(
    viewModel: RankingViewModel = hiltViewModel()
) {
    val topMatches by viewModel.topMatches.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyTop.copy(alpha = 0.55f))
    ) {
        if (topMatches.isEmpty()) {
            EmptyRanking()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                item {
                    RankingHeader(totalScore = totalScore)
                }
                items(topMatches) { match ->
                    RankingItem(match)
                }
            }
        }
    }
}

@Composable
private fun RankingHeader(totalScore: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.ranking_title),
            style = MaterialTheme.typography.titleLarge,
            color = PinRed,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = DarkBlue,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append(stringResource(R.string.ranking_total_score))
                }
                withStyle(
                    SpanStyle(
                        color = AccentYellow,
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    )
                ) {
                    append(totalScore.toString())
                }
            },
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun RankingItem(match: RankingMatch) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = when (match.match.status) {
                    Status.COMPLETED -> stringResource(R.string.ranking_status_completed)
                    Status.INCOMPLETE -> stringResource(R.string.ranking_status_incomplete)
                },
                style = MaterialTheme.typography.titleMedium,
                color = DarkBlue,
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Normal
            )
            match.guessedLocation?.let {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = DarkBlue,
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append(stringResource(R.string.ranking_your_guess))
                        }
                        withStyle(
                            SpanStyle(
                                color = DarkBlue,
                                fontFamily = NunitoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(it)
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = DarkBlue,
                            fontFamily = NunitoFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(stringResource(R.string.ranking_score))
                    }
                    withStyle(
                        SpanStyle(
                            color = AccentYellow,
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    ) {
                        append(match.match.score.toString())
                    }
                },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = match.match.datePlayed.formatDateTime(),
                style = MaterialTheme.typography.bodySmall,
                color = DarkBlue,
                fontFamily = NunitoFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun EmptyRanking() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.ranking_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = DarkBlue
        )
    }
}

private fun Long.formatDateTime(): String {
    return SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault()).format(Date(this))
}
