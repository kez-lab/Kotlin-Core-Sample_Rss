package io.github.kez_lab.kotlinxrpc.sample

/**
 * 앱의 환경 설정을 관리하는 객체
 * 개발, 스테이징, 프로덕션 등 다양한 환경에 따른 설정을 제공합니다.
 */
object AppConfig {
    // 환경 설정 (개발 환경이면 true, 배포 환경이면 false)
    val isDevelopment = true

    // RPC 서버 설정
    object RpcServer {
        // 개발 환경 설정
        object Development {
            const val PROTOCOL = "ws" // WebSocket
            const val HOST = "localhost"
            const val PORT = 8081
            const val PATH = "/rss"
        }

        // 배포 환경 설정
        object Production {
            const val PROTOCOL = "wss" // WebSocket Secure
            const val HOST = "kezlab.site"
            const val PORT = 443
            const val PATH = "/rss"
        }
    }
}