package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.repository.NewsRepository
import kotlin.coroutines.CoroutineContext

class NewsServiceImpl(
    private val newsRepository: NewsRepository,
    override val coroutineContext: CoroutineContext
) : NewsService {

    override suspend fun getLatestNews(): List<News> {
        return newsRepository.getCachedNews()
    }
}