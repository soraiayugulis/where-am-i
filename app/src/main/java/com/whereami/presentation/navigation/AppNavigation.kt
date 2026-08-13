package com.whereami.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whereami.presentation.error.ErrorScreen
import com.whereami.presentation.game.GuessMapScreen
import com.whereami.presentation.game.StreetViewScreen
import com.whereami.presentation.ranking.RankingScreen
import com.whereami.presentation.home.HomeScreen
import com.whereami.presentation.result.ResultScreen
import com.whereami.presentation.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val PLAY = "play"
    const val GUESS = "guess"
    const val RESULT = "result"
    const val RANKING = "ranking"
    const val SETTINGS = "settings"
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
                onRanking = { navController.navigate(Routes.RANKING) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
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
            ResultScreen(
                onHome = { navController.navigateToHome() },
                onPlayAgain = { navController.navigateToPlay() }
            )
        }
        composable(Routes.RANKING) {
            RankingScreen()
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
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

private fun NavHostController.navigateToPlay() {
    navigate(Routes.PLAY) {
        popUpTo(Routes.HOME) { inclusive = false }
    }
}

private fun NavHostController.navigateToHome() {
    navigate(Routes.HOME) {
        popUpTo(Routes.HOME) { inclusive = true }
    }
}
