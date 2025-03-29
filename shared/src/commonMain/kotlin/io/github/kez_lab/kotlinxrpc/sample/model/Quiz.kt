package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)