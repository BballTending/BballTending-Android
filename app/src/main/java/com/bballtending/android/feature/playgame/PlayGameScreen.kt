package com.bballtending.android.feature.playgame

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bballtending.android.R
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDataParamType
import com.bballtending.android.domain.timer.model.TimerState
import com.bballtending.android.feature.playgame.dialog.PlayGameBackButtonDialog
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreviewLandscape
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray

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

    var backButtonEnable by remember { mutableStateOf(true) }
    BackHandler(backButtonEnable) {
        viewModel.setEvent(PlayGameContract.Event.OnBackButtonClicked)
        backButtonEnable = false
    }
    PlayGameScreen(
        curPlayTimeUnitMin = uiState.curPlayTimeUnitMin,
        curPlayTimeUnitSec = uiState.curPlayTimeUnitSec,
        curTimerState = uiState.curTimerState,
        curQuarter = uiState.curQuarter,
        maxQuarter = uiState.maxQuarter,
        onTimerClicked = {
            DLog.d(PLAY_GAME_SCREEN_ROUTE, "uiState.curTimerState=${uiState.curTimerState}")
            when (uiState.curTimerState) {
                TimerState.Ready -> viewModel.setEvent(PlayGameContract.Event.OnGameStart)
                is TimerState.Running -> viewModel.setEvent(PlayGameContract.Event.OnGamePause)
                TimerState.Pause -> viewModel.setEvent(PlayGameContract.Event.OnGameResume)
                else -> {}
            }
        }
    )

    when (uiState.dialogState) {
        PlayGameContract.DialogState.NONE -> Unit

        PlayGameContract.DialogState.BACK_BUTTON_DIALOG -> {
            PlayGameBackButtonDialog(
                onResume = {
                    viewModel.setEvent(PlayGameContract.Event.OnBackButtonDialogDismiss)
                    backButtonEnable = true
                },
                onFinish = {
                    onFinish()
                }
            )
        }
    }
}

@Composable
private fun PlayGameScreen(
    curPlayTimeUnitMin: Int,
    curPlayTimeUnitSec: Int,
    curTimerState: TimerState,
    curQuarter: Int,
    maxQuarter: Int,
    onTimerClicked: () -> Unit
) {
    BballTendingTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background)
        ) {
            val (timerComponent) = createRefs()

            TimerComponent(
                curPlayTimeUnitMin = curPlayTimeUnitMin,
                curPlayTimeUnitSec = curPlayTimeUnitSec,
                curTimerState = curTimerState,
                curQuarter = curQuarter,
                maxQuarter = maxQuarter,
                onTimerClicked = onTimerClicked,
                modifier = Modifier.constrainAs(timerComponent) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
            )
        }
    }
}

@Composable
private fun TimerComponent(
    curPlayTimeUnitMin: Int,
    curPlayTimeUnitSec: Int,
    curTimerState: TimerState,
    curQuarter: Int,
    maxQuarter: Int,
    onTimerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timerInfoMsg = when (curTimerState) {
        TimerState.Uninitialized -> ""

        TimerState.Ready -> stringResource(
            R.string.playGame_timerInfo_startGame,
            curQuarter
        )

        TimerState.Resume,
        TimerState.Start,
        is TimerState.Running -> stringResource(R.string.playGame_timerInfo_pause)

        TimerState.Pause -> stringResource(R.string.playGame_timerInfo_resume)

        TimerState.Finish -> {
            if (curQuarter == maxQuarter) {
                stringResource(R.string.playGame_timerInfo_endGame)
            } else {
                stringResource(
                    R.string.playGame_timerInfo_endQuarter,
                    curQuarter
                )
            }
        }
    }
    val playTimeMin =
        if (curPlayTimeUnitMin >= 10) curPlayTimeUnitMin.toString() else "0$curPlayTimeUnitMin"
    val playTimeSec =
        if (curPlayTimeUnitSec >= 10) curPlayTimeUnitSec.toString() else "0$curPlayTimeUnitSec"
    val timeTextColor = when (curTimerState) {
        TimerState.Uninitialized,
        TimerState.Ready,
        TimerState.Pause,
        TimerState.Finish -> TextHintGray

        else -> TextBlack
    }

    BballTendingTheme {
        ConstraintLayout(
            modifier = modifier
                .noRippleClickable(onTimerClicked)
                .wrapContentSize()
        ) {
            val (infoMsgRef, minTextRef, secTextRef, dividerRef) = createRefs()
            Text(
                text = timerInfoMsg,
                modifier = Modifier
                    .constrainAs(infoMsgRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top)
                    },
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 14.sp,
                    color = TextHintGray
                )
            )
            Text(
                text = playTimeMin,
                modifier = Modifier
                    .constrainAs(minTextRef) {
                        centerVerticallyTo(dividerRef)
                        end.linkTo(dividerRef.start, margin = 14.dp)
                    },
                style = BballTendingTheme.typography.black.copy(
                    fontSize = 60.sp,
                    color = timeTextColor
                )
            )
            Image(
                painter = painterResource(id = R.drawable.icon_time_dividor),
                contentDescription = ":",
                modifier = Modifier
                    .widthIn(min = 12.dp)
                    .heightIn(min = 32.dp)
                    .constrainAs(dividerRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(infoMsgRef.bottom, margin = 25.dp)
                    },
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(color = timeTextColor)
            )
            Text(
                text = playTimeSec,
                modifier = Modifier
                    .constrainAs(secTextRef) {
                        centerVerticallyTo(dividerRef)
                        start.linkTo(dividerRef.end, margin = 14.dp)
                    },
                style = BballTendingTheme.typography.black.copy(
                    fontSize = 60.sp,
                    color = timeTextColor
                )
            )
        }
    }
}

@DevicePreviewLandscape
@Composable
fun PlayGameScreenPreview() {
    BballTendingTheme {
        PlayGameScreen(
            curPlayTimeUnitMin = 8,
            curPlayTimeUnitSec = 12,
            curTimerState = TimerState.Running(8, 12),
            curQuarter = 1,
            maxQuarter = 4,
            onTimerClicked = {}
        )
    }
}