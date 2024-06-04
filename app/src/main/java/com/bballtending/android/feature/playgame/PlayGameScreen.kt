package com.bballtending.android.feature.playgame

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDataParamType
import com.bballtending.android.feature.playgame.dialog.PlayGameBackButtonDialog

const val PLAY_GAME_SCREEN_ROUTE: String = "play_game"
const val PLAY_GAME_DATA_ARGS: String = "game_data"
const val PLAY_GAME_SCREEN_URI: String = "$PLAY_GAME_SCREEN_ROUTE/{$PLAY_GAME_DATA_ARGS}"

fun NavGraphBuilder.playGameScreen(
    onFinish: () -> Unit
) {
    composable(
        route = PLAY_GAME_SCREEN_URI,
        arguments = listOf(
            navArgument(PLAY_GAME_DATA_ARGS) { type = GameDataParamType() }
        )
    ) {
        PlayGameScreen(
            onFinish = onFinish,
            navBackStackEntry = it
        )
    }
}

@Composable
private fun PlayGameScreen(
    onFinish: () -> Unit,
    navBackStackEntry: NavBackStackEntry
) {
    val gameData = navBackStackEntry.arguments?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            it.getParcelable(PLAY_GAME_DATA_ARGS, GameData::class.java)
        else
            it.getParcelable(PLAY_GAME_DATA_ARGS)
    } ?: return onFinish()

    val playGameViewModel: PlayGameViewModel = hiltViewModel()
    playGameViewModel.setEvent(PlayGameContract.Event.OnInitGameData(gameData))
    PlayGameScreen(onFinish = onFinish, viewModel = playGameViewModel)
}

@Composable
private fun PlayGameScreen(
    onFinish: () -> Unit,
    viewModel: PlayGameViewModel = hiltViewModel()
) {
    val uiState: PlayGameContract.PlayGameUiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackPressed(onFinish)
}

@Composable
private fun PlayGameScreen() {

}

@Composable
private fun BackPressed(onFinish: () -> Unit) {
    var dialogVisible by remember { mutableStateOf(false) }

    BackHandler {
        dialogVisible = true
    }

    if (dialogVisible) {
        PlayGameBackButtonDialog(
            onResume = {
                dialogVisible = false
            },
            onFinish = {
                dialogVisible = false
                onFinish()
            }
        )
    }
}