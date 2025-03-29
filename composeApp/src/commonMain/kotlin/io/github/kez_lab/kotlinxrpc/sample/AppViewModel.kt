package io.github.kez_lab.kotlinxrpc.sample

import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import io.github.kez_lab.kotlinxrpc.sample.model.QuizResult
import io.github.kez_lab.kotlinxrpc.sample.network.QuizNetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed interface QuizEffect {
    data object QuizCompleted : QuizEffect
    data object QuizReset : QuizEffect
}


class AppViewModel(private val manager: QuizNetworkManager) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + viewModelJob)
    private val userAnswerList = arrayListOf<Int>()

    private val _quizEffect = MutableSharedFlow<QuizEffect>()
    val quizEffect = _quizEffect.asSharedFlow()

    val isLoading: StateFlow<Boolean>
        field = MutableStateFlow(true)

    val quiz: StateFlow<List<Quiz>>
        field = MutableStateFlow(emptyList())

    val quizResult: StateFlow<QuizResult>
        field = MutableStateFlow(QuizResult(0))

    val currentQuizIndex: StateFlow<Int>
        field = MutableStateFlow(0)

    fun initQuiz() {
        viewModelScope.launch {
            isLoading.value = true
            manager.init()
            loadQuiz()
            isLoading.value = false
        }
    }

    private suspend fun loadQuiz() {
        try {
            quiz.value = manager.fetchQuizzes()
            userAnswerClear()
        } catch (e: Exception) {
            println("Error loading quiz: ${e.message}")
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            try {
                quizResult.value = manager.submitAnswers(userAnswerList)
                sendEffect(QuizEffect.QuizCompleted)
            } catch (e: Exception) {
                println("Error submitting answers: ${e.message}")
            }
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }

    fun selectedAnswer(
        currentPage: Int,
        answerIndex: Int
    ) {
        if (currentPage <= quiz.value.lastIndex) {
            userAnswerList[currentPage] = answerIndex
        }

        if (currentPage == quiz.value.lastIndex) {
            submitAnswers()
        }
    }

    fun onPageChanged(page: Int) {
        currentQuizIndex.value = page
    }

    fun resetQuiz() {
        currentQuizIndex.value = 0
        userAnswerClear()
        sendEffect(QuizEffect.QuizReset)
    }

    private fun sendEffect(effect: QuizEffect) {
        viewModelScope.launch {
            _quizEffect.emit(effect)
        }
    }

    private fun userAnswerClear() {
        userAnswerList.clear()
        userAnswerList.addAll(List(quiz.value.size) { -1 })
    }
}
