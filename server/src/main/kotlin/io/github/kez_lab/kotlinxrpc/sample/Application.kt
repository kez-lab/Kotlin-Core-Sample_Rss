package io.github.kez_lab.kotlinxrpc.sample

import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.repository.NewsRepository
import io.github.kez_lab.kotlinxrpc.sample.repository.NewsRepositoryImpl
import io.github.kez_lab.kotlinxrpc.sample.service.NewsService
import io.github.kez_lab.kotlinxrpc.sample.service.NewsServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.rpc.krpc.ktor.server.Krpc
import kotlinx.rpc.krpc.ktor.server.rpc
import kotlinx.rpc.krpc.serialization.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicReference

// NewsRepository를 전역 인스턴스로 생성
private lateinit var newsRepository: NewsRepository

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    // NewsRepository 초기화
    newsRepository = NewsRepositoryImpl(log)

    installCORS()
    scheduleRssRefresh()
    configureRPC()
    configureRouting()
}

fun Application.configureRPC() {
    install(Krpc)
    routing {
        rpc("/rss") {
            rpcConfig {
                serialization {
                    json(Json { prettyPrint = true })
                }
            }
            registerService<NewsService> { coroutineContext ->
                NewsServiceImpl(newsRepository, coroutineContext)
            }
        }
    }
}

fun Application.installCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Upgrade)
        allowCredentials = true

        allowHost("kez-lab.github.io")
    }
}

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })

    }

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/rss-sample") {
            call.respond(newsRepository.getCachedNews())
        }
    }
}

fun Application.scheduleRssRefresh() {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 2시간마다 실행
    scope.launch {
        while (isActive) {
            newsRepository.refreshRss()
            delay(60 * 60 * 1000 * 2) // 2시간
        }
    }
}