package io.github.kez_lab.kotlinxrpc.sample.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kez_lab.kotlinxrpc.sample.AppViewModel

@Composable
fun ResultScreen(
    viewModel: AppViewModel,
    onClickRestart: () -> Unit = {}
) {
    val quizResult by viewModel.quizResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Score ${quizResult.correctAnswers}",
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(onClick = onClickRestart) {
            Text("Restart Quiz")
        }
    }
}
