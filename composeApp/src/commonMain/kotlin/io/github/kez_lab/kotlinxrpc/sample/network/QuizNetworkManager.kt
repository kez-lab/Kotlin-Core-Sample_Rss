package io.github.kez_lab.kotlinxrpc.sample.network

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import io.github.kez_lab.kotlinxrpc.sample.service.QuizService
import io.ktor.client.HttpClient
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.util.logging.Logger
import io.ktor.utils.io.CancellationException
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

class QuizNetworkManager {
    private lateinit var quizService: QuizService

    suspend fun init() {
        val client = try {
            HttpClient {
                installKrpc()
            }.rpc {
                url {
                    protocol = URLProtocol.WSS
                    host = "kezlab.site"
                    port = 443
                    encodedPath = "/quiz"
                }
                rpcConfig {
                    serialization {
                        json()
                    }
                }
            }
        } catch (e: Exception) {
            println("Error initializing HTTP client: $e")
            null
        }
        quizService = client?.withService<QuizService>() ?: kotlin.run {
            println("Failed to initialize quiz service")
            return
        }
    }

    suspend fun fetchQuizzes(): List<Quiz> {
        return runCatching {
            retryWithReconnect {
                quizService.getQuiz()
            }
        }.onFailure { e ->
            println("Error fetching quizzes: ${e.message}")
        }.getOrThrow()
    }

    suspend fun submitAnswers(answers: List<Int>): QuizResult {
        return runCatching {
            retryWithReconnect {
                quizService.calculateQuizScore(answers)
            }
        }.onFailure { e ->
            println("Error submitting answers: ${e.message}")
        }.getOrThrow()
    }

    private suspend fun reconnect() {
        init()
    }

    private suspend fun <T> retryWithReconnect(
        maxRetries: Int = 3,
        block: suspend () -> T
    ): T {
        var attempt = 0
        while (attempt < maxRetries) {
            try {
                return block()
            } catch (e: CancellationException) {
                println("WebSocket disconnected. Retrying... Attempt: ${attempt + 1}")
                reconnect()
                attempt++
            } catch (e: Exception) {
                throw e // Cancellation 외 다른 예외는 그대로 던짐
            }
        }
        throw IllegalStateException("Max retries reached. Unable to complete the operation.")
    }
}
