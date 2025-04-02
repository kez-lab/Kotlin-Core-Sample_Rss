package io.github.kez_lab.kotlinxrpc.sample.network

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

    private suspend fun <T> executeNewsService(
        block: suspend (NewsService) -> T,
        errorMessage: String
    ): Result<T> {
        return withContext(Dispatchers.Default) {
            val client = createHttpClient()

            try {
                val rpcClient = createRpcClient(client)

                val newsService = rpcClient.withService<NewsService>()
                val result = block(newsService)
                Result.success(result)
            } catch (e: Exception) {
                println("$errorMessage: ${e.message}")
                Result.failure(e)
            } finally {
                client.close()
            }
        }
    }

    override suspend fun fetchNews(): Result<List<News>> {
        return executeNewsService(
            block = { newsService -> newsService.getLatestNews() },
            errorMessage = "뉴스 데이터 가져오기 실패"
        )
    }

    override suspend fun likeNews(link: String): Result<News> {
        return executeNewsService(
            block = { newsService -> newsService.likeNews(link) },
            errorMessage = "뉴스 좋아요 처리 실패"
        )
    }

    private fun createHttpClient() = HttpClient {
        installKrpc()
    }

    private suspend fun createRpcClient(client: HttpClient) = client.rpc {
        url {
            protocol = URLProtocol.WSS
            port = 443
            host = "kezlab.site"
            encodedPath = "/rss"
        }
        rpcConfig {
            serialization {
                json()
            }
        }
    }
}