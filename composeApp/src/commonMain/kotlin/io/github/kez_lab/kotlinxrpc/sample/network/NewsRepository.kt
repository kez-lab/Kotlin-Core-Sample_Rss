package io.github.kez_lab.kotlinxrpc.sample.network

import io.github.kez_lab.kotlinxrpc.sample.model.News

/**
 * 뉴스 관련 네트워크 작업을 처리하는 인터페이스
 */
interface NewsRepository {
    suspend fun fetchNews(): Result<List<News>>
}