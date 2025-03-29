package io.github.kez_lab.kotlinxrpc.sample.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import kotlinrpcsample.composeapp.generated.resources.IBMPlexSansKR_Medium
import kotlinrpcsample.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val font = Font(Res.font.IBMPlexSansKR_Medium)
    MaterialTheme(
        typography = Typography(
            FontFamily(font)
        ),
        content = content
    )
}