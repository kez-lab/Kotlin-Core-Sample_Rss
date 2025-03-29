
package io.github.kez_lab.kotlinxrpc.sample.ui.screen

sealed class Screen(val route: String) {
    data object StartScreen : Screen("start")
    data object QuizScreen : Screen("quiz")
    data object QuizResultResultScreen : Screen("result")
}

