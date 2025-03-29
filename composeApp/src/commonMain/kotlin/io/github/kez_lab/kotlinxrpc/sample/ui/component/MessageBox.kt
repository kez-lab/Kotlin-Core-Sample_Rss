package io.github.kez_lab.kotlinxrpc.sample.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class MessageType {
    INFO,
    WARNING,
    ERROR
}

@Composable
fun MessageBox(
    message: String,
    type: MessageType = MessageType.INFO,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, iconTint) = when (type) {
        MessageType.INFO -> Pair(
            MaterialTheme.colors.primary.copy(alpha = 0.1f),
            MaterialTheme.colors.primary
        )
        MessageType.WARNING -> Pair(
            Color(0xFFFFF3CD),
            Color(0xFFFFB302)
        )
        MessageType.ERROR -> Pair(
            Color(0xFFF8D7DA),
            MaterialTheme.colors.error
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = backgroundColor
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = type.name,
                tint = iconTint,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 16.dp)
            )
            
            Text(
                text = message,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}