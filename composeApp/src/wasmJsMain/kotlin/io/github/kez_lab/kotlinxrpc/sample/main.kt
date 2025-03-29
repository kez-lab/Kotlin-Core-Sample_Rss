package io.github.kez_lab.kotlinxrpc.sample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    document.body!!.style.apply {
        width = "100%"
        margin = "0 auto"
        maxWidth = "360px"
    }
    ComposeViewport(document.body!!) {
        App()
    }
}