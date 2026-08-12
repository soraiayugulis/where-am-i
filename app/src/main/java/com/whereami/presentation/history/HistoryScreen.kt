package com.whereami.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import java.text.DateFormat
import java.util.Date

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val matches by viewModel.matches.collectAsState()

    if (matches.isEmpty()) {
        EmptyHistory()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(matches, key = { it.id }) { match ->
                MatchHistoryItem(match)
            }
        }
    }
}

@Composable
private fun MatchHistoryItem(match: MatchResult) {
    val date = DateFormat.getDateTimeInstance().format(Date(match.datePlayed))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Status: ${match.status}")
            Text("Score: ${match.score}")
            Text("Date: $date")
        }
    }
}

@Composable
private fun EmptyHistory() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No matches yet")
    }
}
