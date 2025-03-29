package io.github.kez_lab.kotlinxrpc.sample.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kez_lab.kotlinxrpc.sample.AppViewModel

@Composable
fun StartScreen(
    viewModel: AppViewModel,
    onStartClick: () -> Unit = {},
) {
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to the Quiz App",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = { onStartClick() },
            modifier = Modifier.wrapContentSize(),
            enabled = !isLoading
        ) {
            val text = if (isLoading) {
                "Loading..."
            } else {
                "Start Quiz"
            }
            Text(text = text)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initQuiz()
    }
}