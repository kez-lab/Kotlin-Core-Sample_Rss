package io.github.kez_lab.kotlinxrpc.sample.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun RemoteImage(
    url: String,
    modifier: Modifier = Modifier,
    description: String = "Remote Image",
    contentScale: ContentScale = ContentScale.Crop
) {
    val painterResource = asyncPainterResource(url)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            resource = painterResource,
            contentDescription = description,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    strokeWidth = 2.dp
                )
            },
            onFailure = {
                Text(
                    text = "이미지 로드 실패",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error
                )
            }
        )
    }
}