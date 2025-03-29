package io.github.kez_lab.kotlinxrpc.sample.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.kez_lab.kotlinxrpc.sample.NewsViewModel
import io.github.kez_lab.kotlinxrpc.sample.model.News
import io.github.kez_lab.kotlinxrpc.sample.ui.component.LoadingIndicator
import io.github.kez_lab.kotlinxrpc.sample.ui.component.MessageBox
import io.github.kez_lab.kotlinxrpc.sample.ui.component.MessageType
import io.github.kez_lab.kotlinxrpc.sample.ui.component.RemoteImage
import kotlinx.coroutines.launch

@Composable
fun NewsListScreen(viewModel: NewsViewModel) {
    val newsList by viewModel.newsList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 1
        }
    }

    Scaffold(
        topBar = {
            NewsTopAppBar(
                onRefreshClick = {
                    viewModel.fetchNews()
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.scrollToItem(0)
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "맨 위로",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colors.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    isLoading && newsList.isEmpty() -> {
                        LoadingIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    error != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            MessageBox(
                                message = error ?: "뉴스를 불러오는 중 오류가 발생했습니다.",
                                type = MessageType.ERROR,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }

                    newsList.isEmpty() -> {
                        MessageBox(
                            message = "표시할 뉴스가 없습니다.",
                            type = MessageType.INFO,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Text(
                                    text = "최신 뉴스",
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            items(newsList) { news ->
                                NewsCard(news = news)
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }

                        AnimatedVisibility(
                            visible = isLoading,
                            modifier = Modifier.align(Alignment.TopCenter),
                            enter = fadeIn(),
                            exit = fadeOut(animationSpec = tween(durationMillis = 500))
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp),
                                elevation = 4.dp
                            ) {
                                Text(
                                    text = "뉴스 업데이트 중...",
                                    style = MaterialTheme.typography.caption,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsTopAppBar(onRefreshClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.height(
            56.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "뉴스",
                    tint = MaterialTheme.colors.onPrimary
                )
                Text(
                    text = " 실시간 뉴스",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onRefreshClick) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "새로고침",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 4.dp
    )
}

@Composable
fun NewsCard(news: News) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            news.imageUrl?.let { url ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    RemoteImage(
                        url = url,
                        description = "뉴스 이미지"
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = news.title ?: "제목 없음",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            news.description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.body2,
                    maxLines = if (expanded) 10 else 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                news.creator?.let { creator ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(MaterialTheme.colors.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = creator.firstOrNull()?.toString() ?: "?",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = creator,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                news.pubDate?.let { date ->
                    Text(
                        text = formatDateString(date),
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatDateString(dateStr: String): String {
    return try {
        val parts = dateStr.split(" ")
        if (parts.size >= 5) {
            "${parts[1]} ${parts[2]} ${parts[3]}"
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}