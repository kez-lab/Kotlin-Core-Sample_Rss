package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc

@Rpc
interface QuizService : RemoteService {
    suspend fun getQuiz(): List<Quiz>
    suspend fun calculateQuizScore(answers: List<Int>): QuizResult
}