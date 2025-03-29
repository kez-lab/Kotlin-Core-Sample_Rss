package io.github.kez_lab.kotlinxrpc.sample

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.service.NewsService
import io.ktor.client.HttpClient
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.rpc.krpc.ktor.client.installKrpc
import kotlinx.rpc.krpc.ktor.client.rpc
import kotlinx.rpc.krpc.ktor.client.rpcConfig
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.rpc.withService

class NewsViewModel {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + viewModelJob)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList: StateFlow<List<News>> = _newsList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val client = HttpClient {
                    installKrpc()
                }.rpc {
                    url {
                        if (AppConfig.isDevelopment) {
                            protocol = URLProtocol.WS
                            host = AppConfig.RpcServer.Development.HOST
                            port = AppConfig.RpcServer.Development.PORT
                            encodedPath = AppConfig.RpcServer.Development.PATH
                        } else {
                            protocol = URLProtocol.WSS
                            host = AppConfig.RpcServer.Production.HOST
                            port = AppConfig.RpcServer.Production.PORT
                            encodedPath = AppConfig.RpcServer.Production.PATH
                        }
                    }
                    rpcConfig {
                        serialization {
                            json()
                        }
                    }
                }

                val newsService = client.withService<NewsService>()
                val latestNews = newsService.getLatestNews()
                _newsList.value = latestNews

            } catch (e: Exception) {
                _error.value = "뉴스를 불러오는 중 오류가 발생했습니다: ${e.message}"
                println("뉴스 불러오기 실패: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clear() {
        viewModelJob.cancel()
    }
}