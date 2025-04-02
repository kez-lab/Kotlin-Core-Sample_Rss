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

    // Store liked news links locally
    private val likedNews = mutableSetOf<String>()
    val likedNewsSet: Set<String> get() = likedNews

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

    fun likeNews(link: String) {
        // If already liked by this session, ignore
        if (link in likedNews) return

        // Add to local liked set to prevent multiple likes from same session
        likedNews.add(link)

        viewModelScope.launch {
            newsRepository.likeNews(link)
                .onSuccess { updatedNews ->
                    // Update the news item in the list
                    val currentList = _newsList.value.toMutableList()
                    val index = currentList.indexOfFirst { it.link == link }
                    if (index != -1) {
                        currentList[index] = updatedNews
                        _newsList.value = currentList
                    }
                }
                .onFailure { throwable ->
                    // If failed, remove from local liked set
                    likedNews.remove(link)
                    println("뉴스 좋아요 처리 실패: ${throwable.message}")
                }
        }
    }

    fun isNewsLiked(link: String): Boolean {
        return link in likedNews
    }
}