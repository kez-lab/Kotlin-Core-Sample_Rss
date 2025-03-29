package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.rssCache
import kotlin.coroutines.CoroutineContext

class NewsServiceImpl(
    override val coroutineContext: CoroutineContext
) : NewsService {

    override suspend fun getLatestNews(): List<News> {
        return rssCache.get()
    }
}