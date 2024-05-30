package com.bballtending.android.feature.scoreboard

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.feature.scoreboard.model.ScoreboardUiState

const val SCOREBOARD_SCREEN_ROUTE: String = "scoreboard"

fun NavGraphBuilder.scoreboardScreen(
    onFinish: () -> Unit
) {
    composable(
        route = SCOREBOARD_SCREEN_ROUTE
    ) {
        ScoreboardScreen(
            onFinish = onFinish
        )
    }
}

@Composable
private fun ScoreboardScreen(
    onFinish: () -> Unit,
    viewModel: ScoreboardViewModel = hiltViewModel()
) {
    val uiState: ScoreboardUiState by viewModel.uiState.collectAsStateWithLifecycle()
}

@Composable
private fun ScoreboardScreen() {

}

@Composable
private fun BackPressed(onFinish: () -> Unit) {
    BackHandler {

    }
}