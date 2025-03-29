package io.github.kez_lab.kotlinxrpc.sample.network

import io.github.kez_lab.kotlinxrpc.sample.AppConfig
import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.service.NewsService
import io.ktor.client.HttpClient
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

/**
 * NewsRepository 인터페이스의 RPC 구현체
 */
class RpcNewsRepository : NewsRepository {

    override suspend fun fetchNews(): Result<List<News>> {
        return withContext(Dispatchers.Default) {
            val client = HttpClient {
                installKrpc()
            }

            try {
                val rpcClient = client.rpc {
                    url {
                        protocol = URLProtocol.WS
                        port = 8080
                        encodedPath = "/rss"
                    }
                    rpcConfig {
                        serialization {
                            json()
                        }
                    }
                }

                val newsService = rpcClient.withService<NewsService>()
                val latestNews = newsService.getLatestNews()

                Result.success(latestNews)
            } catch (e: Exception) {
                println("뉴스 데이터 가져오기 실패: ${e.message}")
                Result.failure(e)
            } finally {
                client.close()
            }
        }
    }
}