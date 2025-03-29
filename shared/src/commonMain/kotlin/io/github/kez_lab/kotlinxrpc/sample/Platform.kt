package io.github.kez_lab.kotlinxrpc.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform