package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.News
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc

@Rpc
interface NewsService : RemoteService {
    suspend fun getLatestNews(): List<News>
    suspend fun likeNews(link: String): News
}