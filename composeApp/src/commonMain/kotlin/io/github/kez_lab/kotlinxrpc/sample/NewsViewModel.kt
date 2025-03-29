package io.github.kez_lab.kotlinxrpc.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.network.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _newsList = MutableStateFlow<List<News>>(emptyList())
    val newsList: StateFlow<List<News>> = _newsList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            newsRepository.fetchNews()
                .onSuccess { news ->
                    _newsList.value = news
                }
                .onFailure { throwable ->
                    _error.value = "뉴스를 불러오는 중 오류가 발생했습니다: ${throwable.message}"
                    println("뉴스 불러오기 실패: ${throwable.message}")
                }

            _isLoading.value = false
        }
    }
}