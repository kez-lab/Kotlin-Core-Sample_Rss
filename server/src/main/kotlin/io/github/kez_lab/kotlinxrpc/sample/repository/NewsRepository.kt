package io.github.kez_lab.kotlinxrpc.sample.repository

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.model.Rss
import io.github.kez_lab.kotlinxrpc.sample.model.toNewsList
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import nl.adaptivity.xmlutil.serialization.XML
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

interface NewsRepository {
    suspend fun refreshRss()
    fun getCachedNews(): List<News>
    suspend fun incrementLike(link: String): News
}

class NewsRepositoryImpl(
    private val log: Logger,
    private val rssUrl: String = "https://www.yonhapnewstv.co.kr/browse/feed/"
) : NewsRepository {

    private val rssCache = AtomicReference<List<News>>(emptyList())
    private val mutex = Mutex()
    private val likesMap = ConcurrentHashMap<String, Int>()

    override suspend fun refreshRss() {
        val client = HttpClient(CIO)
        try {
            mutex.withLock {
                val response = client.get(rssUrl)
                val rawXml = response.bodyAsText()
                val rss = XML.decodeFromString<Rss>(rawXml)
                log.info("RSS 피드 수신 완료 (${response.bodyAsText().length} bytes)")

                // Apply likes to the new news items
                val newsList = rss.toNewsList().map { news ->
                    val likeCount = likesMap.getOrDefault(news.link, 0)
                    news.copy(likeCount = likeCount)
                }

                rssCache.set(newsList)
                log.info("✅ RSS 피드 갱신 완료 (${rss.channel.item.size}건)")
            }
        } catch (e: Exception) {
            log.error("❌ RSS 피드 갱신 실패", e)
        } finally {
            client.close()
        }
    }

    override fun getCachedNews(): List<News> {
        return rssCache.get().map { news ->
            val likeCount = likesMap.getOrDefault(news.link, 0)
            news.copy(likeCount = likeCount)
        }
    }

    override suspend fun incrementLike(link: String): News {
        return mutex.withLock {
            val currentLikes = likesMap.getOrDefault(link, 0)
            val newLikeCount = currentLikes + 1
            likesMap[link] = newLikeCount

            // Find the news item and return the updated version
            val news = rssCache.get().find { it.link == link }

            if (news != null) {
                val updatedNews = news.copy(likeCount = newLikeCount)

                // Update the cache with the new like count
                val updatedList = rssCache.get().map {
                    if (it.link == link) updatedNews else it
                }
                rssCache.set(updatedList)

                log.info("✅ '${news.title}' 뉴스에 좋아요 추가됨 (총 ${newLikeCount}개)")
                updatedNews
            } else {
                log.error("❌ 해당 링크의 뉴스를 찾을 수 없음: $link")
                throw NoSuchElementException("해당 링크의 뉴스를 찾을 수 없습니다.")
            }
        }
    }
}