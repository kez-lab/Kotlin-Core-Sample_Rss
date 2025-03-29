package io.github.kez_lab.kotlinxrpc.sample.ui.screen

sealed class Screen(val route: String) {
    data object NewsScreen : Screen("news")
}