package io.github.kez_lab.kotlinxrpc.sample.service

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import kotlin.coroutines.CoroutineContext

class QuizServiceImpl(
    override val coroutineContext: CoroutineContext
) : QuizService {
    private val quizList = listOf(
        Quiz(
            question = "안드로이드에서 ViewModel의 기본 목적은 무엇인가요?",
            options = listOf(
                "Activity를 대체한다.",
                "UI 데이터를 저장하고 생명주기 변화에 따라 데이터를 유지한다.",
                "백그라운드 작업을 실행한다.",
                "데이터베이스 작업을 처리한다."
            ),
            correctIndex = 1
        ),
        Quiz(
            question = "CoroutineScope.launch의 기본 Dispatcher는 무엇입니까?",
            options = listOf(
                "Dispatchers.Main",
                "Dispatchers.IO",
                "Dispatchers.Default",
                "Dispatchers.Unconfined"
            ),
            correctIndex = 0
        ),
        Quiz(
            question = "안드로이드에서 RecyclerView의 DiffUtil이 제공하는 주요 이점은 무엇인가요?",
            options = listOf(
                "애니메이션 효과 제공",
                "리스트 항목의 차이를 계산하여 효율적으로 업데이트",
                "UI 레이아웃 자동 생성",
                "리스트의 정렬 기능"
            ),
            correctIndex = 1
        ),
        Quiz(
            question = "Android Hilt는 무엇을 위한 라이브러리인가요?",
            options = listOf(
                "네트워크 통신",
                "Dependency Injection",
                "데이터베이스 관리",
                "멀티쓰레딩"
            ),
            correctIndex = 1
        )
    )

    override suspend fun getQuiz(): List<Quiz> = quizList

    override suspend fun calculateQuizScore(answers: List<Int>): QuizResult {
        var correctAnswers = 0

        // 퀴즈 채점
        quizList.forEachIndexed { index, quiz ->
            val userAnswer = answers.getOrNull(index)
            val isCorrect = userAnswer == quiz.correctIndex
            if (isCorrect) {
                correctAnswers++
            }
        }

        return QuizResult(correctAnswers = correctAnswers)
    }
}
