package io.github.kez_lab.kotlinxrpc.sample.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun RemoteImage(
    url: String,
    modifier: Modifier = Modifier,
    description: String = "Remote Image"
) {
    val painterResource = asyncPainterResource(url)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            resource = painterResource,
            contentDescription = description,
            onLoading = {
                CircularProgressIndicator()
            },
            onFailure = {
                Text("Image load failed.")
            }
        )
    }
}