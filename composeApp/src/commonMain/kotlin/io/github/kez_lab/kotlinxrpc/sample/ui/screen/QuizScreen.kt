package io.github.kez_lab.kotlinxrpc.sample.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kez_lab.kotlinxrpc.sample.AppViewModel
import io.github.kez_lab.kotlinxrpc.sample.model.Quiz
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(viewModel: AppViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val quiz by viewModel.quiz.collectAsState()
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsState()
    val quizSize = quiz.size

    val pagerState = rememberPagerState(
        initialPage = currentQuizIndex,
        pageCount = { quiz.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                viewModel.onPageChanged(page)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LinearProgressIndicator(
            progress = (currentQuizIndex) / (quizSize).toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "${currentQuizIndex + 1} / $quizSize",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            QuestionCard(
                quiz = quiz[page],
                onAnswerSelected = { answerIndex ->
                    coroutineScope.launch {
                        viewModel.selectedAnswer(
                            currentPage = page,
                            answerIndex = answerIndex
                        )
                        if (page < quiz.lastIndex) {
                            pagerState.animateScrollToPage(page + 1)
                        } else {
                            viewModel.submitAnswers()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun QuestionCard(quiz: Quiz, onAnswerSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quiz.question,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        quiz.options.forEachIndexed { index, option ->
            Button(
                onClick = { onAnswerSelected(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = option)
            }
        }
    }
}