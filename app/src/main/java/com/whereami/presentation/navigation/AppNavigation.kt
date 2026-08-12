package com.whereami.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whereami.presentation.error.ErrorScreen
import com.whereami.presentation.game.GuessMapScreen
import com.whereami.presentation.game.StreetViewScreen
import com.whereami.presentation.history.HistoryScreen
import com.whereami.presentation.home.HomeScreen
import com.whereami.presentation.result.ResultScreen

object Routes {
    const val HOME = "home"
    const val PLAY = "play"
    const val GUESS = "guess"
    const val RESULT = "result"
    const val HISTORY = "history"
    const val ERROR = "error"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartGame = { navController.navigate(Routes.PLAY) },
                onHistory = { navController.navigate(Routes.HISTORY) }
            )
        }
        composable(Routes.PLAY) {
            StreetViewScreen(
                onGuess = { navController.navigate(Routes.GUESS) },
                onFinished = { navController.navigateToResult() },
                onHome = { navController.navigateToHome() }
            )
        }
        composable(Routes.GUESS) {
            GuessMapScreen(onSubmit = { navController.navigateToResult() })
        }
        composable(Routes.RESULT) {
            ResultScreen(onHome = { navController.navigateToHome() })
        }
        composable(Routes.HISTORY) {
            HistoryScreen()
        }
        composable(Routes.ERROR) {
            ErrorScreen(onHome = { navController.navigateToHome() })
        }
    }
}

private fun NavHostController.navigateToResult() {
    navigate(Routes.RESULT) {
        popUpTo(Routes.HOME) { inclusive = false }
    }
}

private fun NavHostController.navigateToHome() {
    navigate(Routes.HOME) {
        popUpTo(Routes.HOME) { inclusive = true }
    }
}
