package com.bballtending.android.feature.playgame

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bballtending.android.R
import com.bballtending.android.TestModule
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDataParamType
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerAction
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.timer.model.TimerState
import com.bballtending.android.feature.playgame.component.PlayGameScoreBoard
import com.bballtending.android.feature.playgame.component.PlayerActionTicket
import com.bballtending.android.feature.playgame.dialog.PlayGameBackButtonDialog
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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
        year = uiState.year,
        month = uiState.month,
        day = uiState.day,
        gameType = uiState.gameType,
        curPlayTimeUnitMin = uiState.curPlayTimeUnitMin,
        curPlayTimeUnitSec = uiState.curPlayTimeUnitSec,
        curTimerState = uiState.curTimerState,
        curQuarter = uiState.curQuarter,
        maxQuarter = uiState.maxQuarter,
        homeTeamScore = uiState.homeTeamScore,
        awayTeamScore = uiState.awayTeamScore,
        homeTeamPlayer = uiState.homeTeamPlayer.toImmutableList(),
        awayTeamPlayer = uiState.awayTeamPlayer.toImmutableList(),
        isHomeTeamLeft = uiState.isHomeTeamLeft,
        selectedPlayerState = uiState.selectedPlayerState,
        onTimerClicked = {
            DLog.d(PLAY_GAME_SCREEN_ROUTE, "uiState.curTimerState=${uiState.curTimerState}")
            when (uiState.curTimerState) {
                TimerState.Ready -> viewModel.setEvent(PlayGameContract.Event.OnGameStart)
                is TimerState.Running -> viewModel.setEvent(PlayGameContract.Event.OnGamePause)
                TimerState.Pause -> viewModel.setEvent(PlayGameContract.Event.OnGameResume)
                else -> {}
            }
        },
        onPlayerCardClicked = { playerData, isHomeTeamPlayer ->
            viewModel.setEvent(
                PlayGameContract.Event.OnPlayerCardClicked(
                    playerData,
                    isHomeTeamPlayer
                )
            )
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
    year: Int,
    month: Int,
    day: Int,
    gameType: GameType,
    curPlayTimeUnitMin: Int,
    curPlayTimeUnitSec: Int,
    curTimerState: TimerState,
    curQuarter: Int,
    maxQuarter: Int,
    homeTeamScore: Int,
    awayTeamScore: Int,
    homeTeamPlayer: ImmutableList<PlayerData>,
    awayTeamPlayer: ImmutableList<PlayerData>,
    isHomeTeamLeft: Boolean,
    selectedPlayerState: PlayGameContract.SelectedPlayerState,
    onTimerClicked: () -> Unit,
    onPlayerCardClicked: (PlayerData, Boolean) -> Unit
) {
    val gameTypeText = when (gameType) {
        GameType.FULL_COURT -> "(5X5)"
        GameType.HALF_COURT -> "(3X3)"
    }
    val gameTitle = stringResource(id = R.string.game_title_format, year, month, day).let {
        "$it $gameTypeText"
    }

    BballTendingTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background)
        ) {
            val (titleRef, timerRef, dividerRef, scoreBoardRef, actionRef, timelineRef) = createRefs()
            Text(
                text = gameTitle,
                modifier = Modifier.constrainAs(titleRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top, margin = 30.dp)
                },
                style = BballTendingTheme.typography.bold.copy(fontSize = 14.sp)
            )
            TimerComponent(
                curPlayTimeUnitMin = curPlayTimeUnitMin,
                curPlayTimeUnitSec = curPlayTimeUnitSec,
                curTimerState = curTimerState,
                curQuarter = curQuarter,
                maxQuarter = maxQuarter,
                onTimerClicked = onTimerClicked,
                modifier = Modifier.constrainAs(timerRef) {
                    top.linkTo(titleRef.bottom)
                }
            )
            HorizontalDivider(
                modifier = Modifier
                    .width(100.dp)
                    .constrainAs(dividerRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(timerRef.bottom)
                    },
                color = BorderGray
            )
            PlayGameScoreBoard(
                isHomeTeamLeft = isHomeTeamLeft,
                homeTeamScore = homeTeamScore,
                awayTeamScore = awayTeamScore,
                modifier = Modifier.constrainAs(scoreBoardRef) {
                    top.linkTo(dividerRef.bottom, margin = 25.dp)
                }
            )
            ActionComponent(
                isHomeTeamLeft = isHomeTeamLeft,
                modifier = Modifier.constrainAs(actionRef) {
                    top.linkTo(scoreBoardRef.bottom)
                    bottom.linkTo(timelineRef.top)
                    height = Dimension.fillToConstraints
                }
            )
            TimelineComponent(
                modifier = Modifier.constrainAs(timelineRef) {
                    bottom.linkTo(parent.bottom)
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
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (infoMsgRef, minTextRef, secTextRef, dividerRef, quarterTextRef) = createRefs()
            Text(
                text = timerInfoMsg,
                modifier = Modifier
                    .constrainAs(infoMsgRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top, margin = 20.dp)
                    },
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 10.sp,
                    color = TextHintGray
                )
            )
            Text(
                text = playTimeMin,
                modifier = Modifier
                    .constrainAs(minTextRef) {
                        centerVerticallyTo(dividerRef)
                        end.linkTo(dividerRef.start, margin = 12.dp)
                    },
                style = BballTendingTheme.typography.black.copy(
                    fontSize = 32.sp,
                    color = timeTextColor
                )
            )
            Image(
                painter = painterResource(id = R.drawable.icon_time_divider),
                contentDescription = ":",
                modifier = Modifier
                    .widthIn(min = 6.dp)
                    .heightIn(min = 18.dp)
                    .constrainAs(dividerRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(infoMsgRef.bottom, margin = 15.dp)
                    },
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(color = timeTextColor)
            )
            Text(
                text = playTimeSec,
                modifier = Modifier
                    .constrainAs(secTextRef) {
                        centerVerticallyTo(dividerRef)
                        start.linkTo(dividerRef.end, margin = 12.dp)
                    },
                style = BballTendingTheme.typography.black.copy(
                    fontSize = 32.sp,
                    color = timeTextColor
                )
            )
            Text(
                text = "${curQuarter}Q",
                modifier = Modifier
                    .constrainAs(quarterTextRef) {
                        centerHorizontallyTo(parent)
                        top.linkTo(dividerRef.bottom, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 15.dp)
                    },
                style = BballTendingTheme.typography.medium.copy(fontSize = 12.sp)
            )
        }
    }
}

@Composable
private fun ActionComponent(
    isHomeTeamLeft: Boolean,
    modifier: Modifier = Modifier,
    homeTeamName: String = "Home",
    awayTeamName: String = "Away"
) {
    BballTendingTheme {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(if (isHomeTeamLeft) Alignment.CenterStart else Alignment.CenterEnd)
            ) {
                Text(
                    text = homeTeamName,
                    modifier = Modifier.padding(
                        start = if (isHomeTeamLeft) 15.dp else 0.dp,
                        top = 25.dp,
                        end = if (isHomeTeamLeft) 0.dp else 15.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(fontSize = 14.sp)
                )
                LazyColumn(
                    modifier = Modifier.padding(
                        start = if (isHomeTeamLeft) 15.dp else 0.dp,
                        top = 12.dp,
                        end = if (isHomeTeamLeft) 0.dp else 15.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (action in PlayerAction.values()) {
                        item(key = action) {
                            PlayerActionTicket(
                                isHomeTeam = true,
                                action = action,
                                onPlayerActionTicketClicked = { action, isHomeTeam ->

                                }
                            )
                        }
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.icon_horizontal_switch),
                contentDescription = "Switch Team",
                modifier = Modifier
                    .noRippleClickable {

                    }
                    .align(Alignment.TopCenter)
                    .padding(top = 25.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(if (isHomeTeamLeft) Alignment.CenterEnd else Alignment.CenterStart)
            ) {
                Text(
                    text = awayTeamName,
                    modifier = Modifier
                        .padding(
                            start = if (isHomeTeamLeft) 0.dp else 15.dp,
                            top = 25.dp,
                            end = if (isHomeTeamLeft) 15.dp else 0.dp
                        )
                        .align(Alignment.End),
                    style = BballTendingTheme.typography.bold.copy(fontSize = 14.sp)
                )
                LazyColumn(
                    modifier = Modifier.padding(
                        start = if (isHomeTeamLeft) 0.dp else 15.dp,
                        top = 12.dp,
                        end = if (isHomeTeamLeft) 15.dp else 0.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    for (action in PlayerAction.values()) {
                        item(key = action) {
                            PlayerActionTicket(
                                isHomeTeam = false,
                                action = action,
                                onPlayerActionTicketClicked = { action, isHomeTeam ->

                                },
                                isLeftSide = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineComponent(
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(124.dp)
                .background(BballTendingTheme.colors.background)
                .shadow(elevation = 1.dp)
        ) {
            Text(
                text = "기록",
                modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 14.sp)
            )
            Spacer(modifier = Modifier.height(7.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {

            }
        }
    }
}

@DevicePreview
@Composable
fun PlayGameScreenPreview() {
    BballTendingTheme {
        PlayGameScreen(
            year = 2024,
            month = 7,
            day = 6,
            gameType = GameType.FULL_COURT,
            curPlayTimeUnitMin = 8,
            curPlayTimeUnitSec = 12,
            curTimerState = TimerState.Running(8, 12),
            curQuarter = 1,
            maxQuarter = 4,
            homeTeamScore = 33,
            awayTeamScore = 22,
            homeTeamPlayer = TestModule.createTestData().homeTeamPlayer.toImmutableList(),
            awayTeamPlayer = TestModule.createTestData().awayTeamPlayer.toImmutableList(),
            isHomeTeamLeft = true,
            selectedPlayerState = PlayGameContract.SelectedPlayerState.None,
            onTimerClicked = {},
            onPlayerCardClicked = { _, _ -> }
        )
    }
}