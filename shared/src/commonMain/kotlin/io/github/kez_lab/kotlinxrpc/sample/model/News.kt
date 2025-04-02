package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val title: String,
    val link: String,
    val pubDate: String,
    val creator: String,
    val description: String,
    val imageUrl: String? = null,
)