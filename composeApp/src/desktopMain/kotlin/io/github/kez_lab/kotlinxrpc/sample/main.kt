package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinRPCSample",
    ) {
        App()
    }
}