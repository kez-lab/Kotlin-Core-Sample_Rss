package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val correctAnswers: Int,    // 정답 개수
)