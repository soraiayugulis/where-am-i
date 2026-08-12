package com.whereami.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartGame: () -> Unit = {},
    onHistory: () -> Unit = {}
) {
    val weeklyScore by viewModel.weeklyScore.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Where Am I?",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Weekly Score: $weeklyScore",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = onStartGame) {
            Text("Start Game")
        }
        Button(
            onClick = onHistory,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("History")
        }
    }
}
