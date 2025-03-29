package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val title: String? = null,
    val link: String? = null,
    val pubDate: String? = null,
    val creator: String? = null,
    val description: String? = null,
    val imageUrl: String?
)