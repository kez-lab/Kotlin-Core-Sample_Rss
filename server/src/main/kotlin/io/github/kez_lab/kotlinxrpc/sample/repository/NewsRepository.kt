package io.github.kez_lab.kotlinxrpc.sample.repository

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.model.Rss
import io.github.kez_lab.kotlinxrpc.sample.model.toNewsList
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.application.log
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.adaptivity.xmlutil.serialization.XML
import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicReference

interface NewsRepository {
    suspend fun refreshRss()
    fun getCachedNews(): List<News>
}

class NewsRepositoryImpl(
    private val log: Logger,
    private val rssUrl: String = "https://www.yonhapnewstv.co.kr/browse/feed/"
) : NewsRepository {

    private val rssCache = AtomicReference<List<News>>(emptyList())
    private val mutex = Mutex()

    suspend fun fetchRss() {
        val client = HttpClient(CIO)
        try {
            val response = client.get("https://www.yonhapnewstv.co.kr/browse/feed/")
            val rawXml = response.bodyAsText()
            val rss = XML.decodeFromString<Rss>(rawXml)
            log.info("RSS 피드 수신 완료 (${response.bodyAsText().length} bytes)")
            rssCache.set(rss.toNewsList())
            log.info("✅ RSS 피드 갱신 완료 (${rss.channel.item.size}건)")
        } catch (e: Exception) {
            log.error("❌ RSS 피드 갱신 실패", e)
        } finally {
            client.close()
        }
    }

    override suspend fun refreshRss() {
        val client = HttpClient(CIO)
        try {
            mutex.withLock {
                val response = client.get(rssUrl)
                val rawXml = response.bodyAsText()
                val rss = XML.decodeFromString<Rss>(rawXml)
                log.info("RSS 피드 수신 완료 (${response.bodyAsText().length} bytes)")
                rssCache.set(rss.toNewsList())
                log.info("✅ RSS 피드 갱신 완료 (${rss.channel.item.size}건)")
            }
        } catch (e: Exception) {
            log.error("❌ RSS 피드 갱신 실패", e)
        } finally {
            client.close()
        }
    }

    override fun getCachedNews(): List<News> {
        return rssCache.get()
    }
}