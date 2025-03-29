package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.kez_lab.kotlinxrpc.sample.QuizEffect.QuizCompleted
import io.github.kez_lab.kotlinxrpc.sample.QuizEffect.QuizReset
import io.github.kez_lab.kotlinxrpc.sample.network.QuizNetworkManager
import io.github.kez_lab.kotlinxrpc.sample.ui.QuizTheme
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.QuizScreen
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.ResultScreen
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.Screen
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.StartScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val appViewModel = AppViewModel(QuizNetworkManager())

@Composable
fun App() {
    QuizTheme {
        QuizApp()
    }
}

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen.route
    ) {
        composable(Screen.StartScreen.route) {
            StartScreen(
                viewModel = appViewModel,
                onStartClick = {
                    navController.navigate(Screen.QuizScreen.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = false }
                    }
                }
            )
        }
        composable(Screen.QuizScreen.route) { backStackEntry ->
            QuizScreen(viewModel = appViewModel)
        }
        composable(Screen.QuizResultResultScreen.route) { backStackEntry ->
            ResultScreen(
                viewModel = appViewModel,
                onClickRestart = {
                    appViewModel.resetQuiz()
                    navController.navigate(Screen.QuizScreen.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = false }
                    }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        appViewModel.quizEffect.onEach { state ->
            when (state) {
                QuizReset -> {
                    navController.navigate(Screen.QuizScreen.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = false }
                    }
                }

                QuizCompleted -> {
                    navController.navigate(Screen.QuizResultResultScreen.route) {
                        popUpTo(Screen.StartScreen.route) { inclusive = false }
                    }
                }
            }
        }.launchIn(coroutineScope)
    }
}
