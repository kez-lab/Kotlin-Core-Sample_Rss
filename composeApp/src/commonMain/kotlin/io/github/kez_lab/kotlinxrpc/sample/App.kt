package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.kez_lab.kotlinxrpc.sample.ui.AppTheme
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.NewsListScreen
import io.github.kez_lab.kotlinxrpc.sample.ui.screen.Screen

private val newsViewModel = NewsViewModel()

@Composable
fun App() {
    AppTheme {
        NewsApp()
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.NewsScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.NewsScreen.route) {
                NewsListScreen(viewModel = newsViewModel)
            }
        }
    }
}