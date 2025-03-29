package io.github.kez_lab.kotlinxrpc.sample.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    showText: Boolean = true
) {
    var showLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("로딩 중") }

    LaunchedEffect(Unit) {
        showLoading = true

        if (showText) {
            while (true) {
                loadingText = "로딩 중"
                delay(500)
                loadingText = "로딩 중."
                delay(500)
                loadingText = "로딩 중.."
                delay(500)
                loadingText = "로딩 중..."
                delay(500)
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(72.dp),
            strokeCap = StrokeCap.Round
        )

        if (showText) {
            Text(
                text = loadingText,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}