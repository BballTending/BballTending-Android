package com.bballtending.android.feature.addgame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bballtending.android.R
import com.bballtending.android.TestModule
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.feature.Border
import com.bballtending.android.feature.addgame.component.AddPlayerCard
import com.bballtending.android.feature.addgame.component.PlayerInfoCard
import com.bballtending.android.feature.addgame.model.AddGameUiState
import com.bballtending.android.feature.border
import com.bballtending.android.feature.component.BballTendingTimePicker
import com.bballtending.android.feature.dialog.PlayerInfoDialog
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

const val ADD_GAME_SCREEN_ROUTE: String = "add_game"
const val ADD_GAME_TYPE_ARGS: String = "game_type"
const val ADD_GAME_DATE_YEAR_ARGS: String = "game_date_year"
const val ADD_GAME_DATE_MONTH_ARGS: String = "game_date_month"
const val ADD_GAME_DATE_DAY_ARGS: String = "game_date_day"
const val ADD_GAME_SCREEN_NAV_URI: String =
    "$ADD_GAME_SCREEN_ROUTE/{$ADD_GAME_TYPE_ARGS}/{$ADD_GAME_DATE_YEAR_ARGS}/{$ADD_GAME_DATE_MONTH_ARGS}/{$ADD_GAME_DATE_DAY_ARGS}"

fun NavGraphBuilder.addGameScreen(
    onClose: () -> Unit,
    onStartGame: (GameData) -> Unit
) {
    composable(
        route = ADD_GAME_SCREEN_NAV_URI,
        arguments = listOf(
            navArgument(ADD_GAME_TYPE_ARGS) { type = NavType.IntType },
            navArgument(ADD_GAME_DATE_YEAR_ARGS) { type = NavType.IntType },
            navArgument(ADD_GAME_DATE_MONTH_ARGS) { type = NavType.IntType },
            navArgument(ADD_GAME_DATE_DAY_ARGS) { type = NavType.IntType }
        )
    ) {
        AddGameScreen(
            onClose = onClose,
            onStartGame = onStartGame,
            navBackStackEntry = it
        )
    }
}

@Composable
private fun AddGameScreen(
    onClose: () -> Unit,
    onStartGame: (GameData) -> Unit,
    navBackStackEntry: NavBackStackEntry
) {
    val gameType = (navBackStackEntry.arguments?.getInt(ADD_GAME_TYPE_ARGS) ?: 0)
        .let { gameTypeArgs: Int ->
            GameType.values().first {
                it.ordinal == gameTypeArgs
            }
        }
    val gameDateYear = navBackStackEntry.arguments?.getInt(ADD_GAME_DATE_YEAR_ARGS)
    val gameDateMonth = navBackStackEntry.arguments?.getInt(ADD_GAME_DATE_MONTH_ARGS)
    val gameDateDay = navBackStackEntry.arguments?.getInt(ADD_GAME_DATE_DAY_ARGS)
    val gameDate = if (gameDateYear != null && gameDateMonth != null && gameDateDay != null) {
        GameDate(gameDateYear, gameDateMonth, gameDateDay)
    } else {
        val localDate = LocalDate.now()
        GameDate(localDate.year, localDate.monthValue, localDate.dayOfMonth)
    }

    val addGameViewModel: AddGameViewModel = hiltViewModel()
    addGameViewModel.initData(gameType = gameType, gameDate = gameDate)

    AddGameScreen(onClose = onClose, onStartGame = onStartGame, addGameViewModel = addGameViewModel)
}

@Composable
private fun AddGameScreen(
    onClose: () -> Unit,
    onStartGame: (GameData) -> Unit,
    addGameViewModel: AddGameViewModel = hiltViewModel()
) {
    val uiState: AddGameUiState by addGameViewModel.uiState.collectAsStateWithLifecycle()
    val createdGameData: GameData? by addGameViewModel.createdGameData.collectAsStateWithLifecycle()

    LaunchedEffect(createdGameData) {
        createdGameData?.let(onStartGame)
    }

    AddGameScreen(
        playingNow = uiState.playingNow,
        hour = uiState.hour,
        minute = uiState.minute,
        gameType = uiState.gameType,
        quarter = uiState.quarter,
        quarterMinusEnable = uiState.quarterMinusEnable,
        quarterPlusEnable = uiState.quarterPlusEnable,
        playTime = uiState.playTime,
        playTimeMinusEnable = uiState.playTimeMinusEnable,
        playTimePlusEnable = uiState.playTimePlusEnable,
        breakTime = uiState.breakTime,
        breakTimeMinusEnable = uiState.breakTimeMinusEnable,
        breakTimePlusEnable = uiState.breakTimePlusEnable,
        targetScore = uiState.targetScore,
        targetScoreMinusEnable = uiState.targetScoreMinusEnable,
        targetScorePlusEnable = uiState.targetScorePlusEnable,
        homeTeamPlayer = uiState.homeTeamPlayer.toImmutableList(),
        awayTeamPlayer = uiState.awayTeamPlayer.toImmutableList(),
        startGameEnable = uiState.startGameEnable,
        onPlayingNowSelect = addGameViewModel::onPlayingNowSelect,
        onHourChanged = addGameViewModel::onHourChanged,
        onMinuteChanged = addGameViewModel::onMinuteChanged,
        onGameTypeSelect = addGameViewModel::onGameTypeSelect,
        onQuarterChange = addGameViewModel::onQuarterChange,
        onPlayTimeChange = addGameViewModel::onPlayTimeChange,
        onBreakTimeChange = addGameViewModel::onBreakTimeChange,
        onTargetScoreChange = addGameViewModel::onTargetScoreChange,
        onPlayerAdded = addGameViewModel::onPlayerAdded,
        onPlayerModified = addGameViewModel::onPlayerModified,
        onPlayerRemoved = addGameViewModel::onPlayerRemoved,
        onStartGame = addGameViewModel::onStartGame,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGameScreen(
    playingNow: Boolean,
    hour: Int,
    minute: Int,
    gameType: GameType,
    quarter: Int,
    quarterMinusEnable: Boolean,
    quarterPlusEnable: Boolean,
    playTime: Int,
    playTimeMinusEnable: Boolean,
    playTimePlusEnable: Boolean,
    breakTime: Int,
    breakTimeMinusEnable: Boolean,
    breakTimePlusEnable: Boolean,
    targetScore: Int,
    targetScoreMinusEnable: Boolean,
    targetScorePlusEnable: Boolean,
    homeTeamPlayer: ImmutableList<PlayerData>,
    awayTeamPlayer: ImmutableList<PlayerData>,
    startGameEnable: Boolean,
    onPlayingNowSelect: (Boolean) -> Unit,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    onGameTypeSelect: (GameType) -> Unit,
    onQuarterChange: (Int) -> Unit,
    onPlayTimeChange: (Int) -> Unit,
    onBreakTimeChange: (Int) -> Unit,
    onTargetScoreChange: (Int) -> Unit,
    onPlayerAdded: (Boolean, String, String, Position) -> Boolean,
    onPlayerModified: (Boolean, PlayerData) -> Boolean,
    onPlayerRemoved: (Boolean, PlayerData) -> Unit,
    onStartGame: () -> Unit,
    onClose: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    var addPlayerDialogVisible by remember { mutableStateOf(false) }
    var modifyPlayerData by remember { mutableStateOf<PlayerData?>(null) }
    var isHomeTeamPlayer by remember { mutableStateOf(false) }

    BballTendingTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.addGame_title),
                            style = BballTendingTheme.typography.medium.copy(fontSize = 18.sp)
                        )
                    },
                    modifier = Modifier
                        .border(
                            strokeWidth = 1.dp,
                            color = BorderGray,
                            borderList = listOf(Border.BOTTOM)
                        ),
                    navigationIcon = {
                        IconButton(onClick = { onClose() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_close),
                                contentDescription = "Close"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {},
            snackbarHost = {},
            containerColor = BballTendingTheme.colors.onPrimary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
            ) {
                PlayingNowContent(
                    playingNow = playingNow,
                    onPlayingNowSelect = onPlayingNowSelect,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = BballTendingTheme.colors.background)
                        .padding(top = 14.dp)
                )
                if (!playingNow) {
                    GameStartTimeContent(
                        playingNow = playingNow,
                        hour = hour,
                        minute = minute,
                        onHourChanged = onHourChanged,
                        onMinuteChanged = onMinuteChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .background(color = BballTendingTheme.colors.background)
                    )
                }
                GameTypeContent(
                    gameType = gameType,
                    onGameTypeSelect = onGameTypeSelect,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                GameTimeContent(
                    gameType = gameType,
                    quarter = quarter,
                    quarterMinusEnable = quarterMinusEnable,
                    quarterPlusEnable = quarterPlusEnable,
                    playTime = playTime,
                    playTimeMinusEnable = playTimeMinusEnable,
                    playTimePlusEnable = playTimePlusEnable,
                    breakTime = breakTime,
                    breakTimeMinusEnable = breakTimeMinusEnable,
                    breakTimePlusEnable = breakTimePlusEnable,
                    targetScore = targetScore,
                    targetScoreMinusEnable = targetScoreMinusEnable,
                    targetScorePlusEnable = targetScorePlusEnable,
                    onQuarterChange = onQuarterChange,
                    onPlayTimeChange = onPlayTimeChange,
                    onBreakTimeChange = onBreakTimeChange,
                    onTargetScoreChange = onTargetScoreChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                PlayerInfoContent(
                    isHomeTeam = true,
                    playerList = homeTeamPlayer,
                    onAddPlayerCardClick = {
                        isHomeTeamPlayer = true
                        addPlayerDialogVisible = true
                    },
                    onPlayerInfoCardClick = {
                        isHomeTeamPlayer = true
                        modifyPlayerData = it
                    },
                    onPlayerRemoved = onPlayerRemoved,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                PlayerInfoContent(
                    isHomeTeam = false,
                    playerList = awayTeamPlayer,
                    onAddPlayerCardClick = {
                        isHomeTeamPlayer = false
                        addPlayerDialogVisible = true
                    },
                    onPlayerInfoCardClick = {
                        isHomeTeamPlayer = false
                        modifyPlayerData = it
                    },
                    onPlayerRemoved = onPlayerRemoved,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                Button(
                    onClick = {
                        onStartGame()
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    enabled = startGameEnable,
                    shape = RectangleShape,
                    colors = ButtonColors(
                        containerColor = BballTendingTheme.colors.primary,
                        contentColor = BballTendingTheme.colors.primary,
                        disabledContainerColor = BorderGray,
                        disabledContentColor = BorderGray
                    ),
                    contentPadding = PaddingValues(top = 18.dp, bottom = 17.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.msg_play_game),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.bold.copy(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }

        if (addPlayerDialogVisible) {
            PlayerInfoDialog(
                isHomeTeamPlayer = isHomeTeamPlayer,
                recentPlayerList = listOf<PlayerData>().toImmutableList(),
                onAddGamePlayer = onPlayerAdded,
                onDismiss = {
                    addPlayerDialogVisible = false
                }
            )
        } else if (modifyPlayerData != null) {
            modifyPlayerData?.let {
                PlayerInfoDialog(
                    isHomeTeamPlayer = isHomeTeamPlayer,
                    recentPlayerList = listOf<PlayerData>().toImmutableList(),
                    originPlayerData = it,
                    onModifyGamePlayer = onPlayerModified,
                    onDismiss = {
                        modifyPlayerData = null
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayingNowContent(
    playingNow: Boolean,
    onPlayingNowSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedFontStyle =
        BballTendingTheme.typography.medium.copy(fontSize = 15.sp, color = Color.White)
    val unselectedFontStyle =
        BballTendingTheme.typography.regular.copy(fontSize = 15.sp, color = TextHintGray)
    val selectedBgColor = TextBlack
    val unselectedBgColor = BballTendingTheme.colors.background

    BballTendingTheme {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.addGame_playingNowContent_title),
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
            )
            Box(
                modifier = Modifier
                    .noRippleClickable { onPlayingNowSelect(true) }
                    .padding(start = 15.dp, top = 24.dp, end = 15.dp)
                    .fillMaxWidth()
                    .background(color = if (playingNow) selectedBgColor else unselectedBgColor)
                    .then(
                        if (!playingNow)
                            Modifier.border(width = 1.dp, color = BorderGray)
                        else
                            Modifier
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.addGame_playingNowContent_playingNow),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 12.dp),
                    style = if (playingNow) selectedFontStyle else unselectedFontStyle
                )
            }
            Box(
                modifier = Modifier
                    .noRippleClickable { onPlayingNowSelect(false) }
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .background(color = if (!playingNow) selectedBgColor else unselectedBgColor)
                    .then(
                        if (playingNow)
                            Modifier.border(width = 1.dp, color = BorderGray)
                        else
                            Modifier
                    )
            ) {
                Text(
                    text = stringResource(id = R.string.addGame_playingNowContent_lastGame),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 12.dp),
                    style = if (!playingNow) selectedFontStyle else unselectedFontStyle
                )
            }
        }
    }
}

@Composable
private fun GameStartTimeContent(
    playingNow: Boolean,
    hour: Int,
    minute: Int,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = if (playingNow) R.string.addGame_nowGameStartTimeContent_title else R.string.addGame_lastGameStartTimeContent_title),
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
            )
            BballTendingTimePicker(
                initHour = hour,
                initMinute = minute,
                onHourChanged = onHourChanged,
                onMinuteChanged = onMinuteChanged,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun GameTypeContent(
    gameType: GameType,
    onGameTypeSelect: (GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedFontStyle =
        BballTendingTheme.typography.medium.copy(fontSize = 15.sp, color = Color.White)
    val unselectedFontStyle =
        BballTendingTheme.typography.regular.copy(fontSize = 15.sp, color = TextHintGray)
    val selectedBgColor = TextBlack
    val unselectedBgColor = BballTendingTheme.colors.background

    BballTendingTheme {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.addGame_gameTypeContent_title),
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
            )
            Row(modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)) {
                Box(
                    modifier = Modifier
                        .noRippleClickable { onGameTypeSelect(GameType.FULL_COURT) }
                        .padding(start = 15.dp)
                        .wrapContentSize()
                        .background(if (gameType == GameType.FULL_COURT) selectedBgColor else unselectedBgColor)
                        .then(
                            if (gameType != GameType.FULL_COURT)
                                Modifier.border(width = 1.dp, color = BorderGray)
                            else
                                Modifier
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.gameType_fullCourt),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 12.dp),
                        style = if (gameType == GameType.FULL_COURT) selectedFontStyle else unselectedFontStyle
                    )
                }
                Box(
                    modifier = Modifier
                        .noRippleClickable { onGameTypeSelect(GameType.HALF_COURT) }
                        .padding(start = 15.dp)
                        .wrapContentSize()
                        .background(if (gameType == GameType.HALF_COURT) selectedBgColor else unselectedBgColor)
                        .then(
                            if (gameType != GameType.HALF_COURT)
                                Modifier.border(width = 1.dp, color = BorderGray)
                            else
                                Modifier
                        )
                ) {
                    Text(
                        text = stringResource(id = R.string.gameType_halfCourt),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 12.dp),
                        style = if (gameType == GameType.HALF_COURT) selectedFontStyle else unselectedFontStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun GameTimeContent(
    gameType: GameType,
    quarter: Int,
    quarterMinusEnable: Boolean,
    quarterPlusEnable: Boolean,
    playTime: Int,
    playTimeMinusEnable: Boolean,
    playTimePlusEnable: Boolean,
    breakTime: Int,
    breakTimeMinusEnable: Boolean,
    breakTimePlusEnable: Boolean,
    targetScore: Int,
    targetScoreMinusEnable: Boolean,
    targetScorePlusEnable: Boolean,
    onQuarterChange: (Int) -> Unit,
    onPlayTimeChange: (Int) -> Unit,
    onBreakTimeChange: (Int) -> Unit,
    onTargetScoreChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.addGame_gameTimeContent_title),
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 15.dp, top = 14.dp, end = 15.dp),
                thickness = 1.dp,
                color = BorderGray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.addGame_gameTimeContent_quarter),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.CenterVertically),
                    style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
                )

                Row {
                    IconButton(
                        onClick = {
                            onQuarterChange(-1)
                        },
                        modifier = Modifier,
                        enabled = quarterMinusEnable
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_minus),
                            contentDescription = "Minus"
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.quarter_format, quarter),
                        modifier = Modifier
                            .widthIn(min = 80.dp)
                            .align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = {
                            onQuarterChange(1)
                        },
                        enabled = quarterPlusEnable
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_plus),
                            contentDescription = "Plus"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.addGame_gameTimeContent_playTime),
                    modifier = Modifier
                        .padding(start = 15.dp, bottom = 7.dp)
                        .align(Alignment.CenterVertically),
                    style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
                )

                Row {
                    IconButton(
                        onClick = {
                            onPlayTimeChange(-1)
                        },
                        modifier = Modifier,
                        enabled = playTimeMinusEnable
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_minus),
                            contentDescription = "Minus"
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.playTime_format, playTime),
                        modifier = Modifier
                            .widthIn(min = 80.dp)
                            .align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = {
                            onPlayTimeChange(1)
                        },
                        enabled = playTimePlusEnable
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_plus),
                            contentDescription = "Plus"
                        )
                    }
                }
            }
            if (quarter > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.addGame_gameTimeContent_breakTime),
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                            .align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
                    )

                    Row {
                        IconButton(
                            onClick = {
                                onBreakTimeChange(-1)
                            },
                            modifier = Modifier,
                            enabled = breakTimeMinusEnable
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_minus),
                                contentDescription = "Minus"
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.breakTime_format, breakTime),
                            modifier = Modifier
                                .widthIn(min = 80.dp)
                                .align(Alignment.CenterVertically),
                            style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp),
                            textAlign = TextAlign.Center
                        )
                        IconButton(
                            onClick = {
                                onBreakTimeChange(1)
                            },
                            enabled = breakTimePlusEnable
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_plus),
                                contentDescription = "Plus"
                            )
                        }
                    }
                }
            }
            if (gameType == GameType.HALF_COURT) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.addGame_gameTimeContent_targetScore),
                        modifier = Modifier
                            .padding(start = 15.dp, bottom = 7.dp)
                            .align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
                    )

                    Row {
                        IconButton(
                            onClick = {
                                onTargetScoreChange(-1)
                            },
                            modifier = Modifier,
                            enabled = targetScoreMinusEnable
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_minus),
                                contentDescription = "Minus"
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.targetScore_format, targetScore),
                            modifier = Modifier
                                .widthIn(min = 80.dp)
                                .align(Alignment.CenterVertically),
                            style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp),
                            textAlign = TextAlign.Center
                        )
                        IconButton(
                            onClick = {
                                onTargetScoreChange(1)
                            },
                            enabled = targetScorePlusEnable
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_plus),
                                contentDescription = "Plus"
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 1.dp, end = 15.dp, bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_info),
                    contentDescription = "Info",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = stringResource(id = R.string.addGame_gameTimeContent_info),
                    style = BballTendingTheme.typography.regular.copy(fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
private fun PlayerInfoContent(
    isHomeTeam: Boolean,
    playerList: ImmutableList<PlayerData>,
    onAddPlayerCardClick: () -> Unit,
    onPlayerInfoCardClick: (PlayerData) -> Unit,
    onPlayerRemoved: (Boolean, PlayerData) -> Unit,
    modifier: Modifier = Modifier
) {
    val teamName = if (isHomeTeam) "홈 팀" else "어웨이 팀"
    BballTendingTheme {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.addGame_playerInfoContent_title, teamName),
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(start = 15.dp, top = 14.dp, end = 15.dp),
                thickness = 1.dp,
                color = BorderGray
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AddPlayerCard(
                        onAddPlayerCardClick = onAddPlayerCardClick
                    )
                }

                for (playerData in playerList) {
                    item {
                        PlayerInfoCard(
                            isHomeTeam = isHomeTeam,
                            playerData = playerData,
                            onPlayerInfoCardClick = onPlayerInfoCardClick,
                            topEndIconResId = R.drawable.icon_close_10dp,
                            onTopEndIconClick = {
                                onPlayerRemoved(isHomeTeam, it)
                            }
                        )
                    }
                }
            }
            Text(
                text = stringResource(
                    id = R.string.addGame_playerInfoContent_totalPlayer,
                    playerList.size
                ),
                modifier = Modifier
                    .padding(top = 1.dp, end = 15.dp, bottom = 24.dp)
                    .align(Alignment.End),
                style = BballTendingTheme.typography.regular.copy(fontSize = 12.sp)
            )
        }
    }
}

@DevicePreview
@Composable
private fun AddGameScreenPreview() {
    val testData = TestModule.createTestData()

    BballTendingTheme {
        AddGameScreen(
            playingNow = true,
            hour = 14,
            minute = 39,
            gameType = GameType.FULL_COURT,
            quarter = 4,
            quarterMinusEnable = true,
            quarterPlusEnable = false,
            playTime = 10,
            playTimeMinusEnable = true,
            playTimePlusEnable = false,
            breakTime = 5,
            breakTimeMinusEnable = true,
            breakTimePlusEnable = true,
            targetScore = 21,
            targetScoreMinusEnable = true,
            targetScorePlusEnable = true,
            homeTeamPlayer = testData.homeTeamPlayer.toImmutableList(),
            awayTeamPlayer = testData.awayTeamPlayer.toImmutableList(),
            startGameEnable = false,
            onPlayingNowSelect = {},
            onHourChanged = {},
            onMinuteChanged = {},
            onGameTypeSelect = {},
            onQuarterChange = {},
            onPlayTimeChange = {},
            onBreakTimeChange = {},
            onTargetScoreChange = {},
            onPlayerAdded = { _, _, _, _ -> true },
            onPlayerModified = { _, _ -> true },
            onPlayerRemoved = { _, _ -> },
            onStartGame = {},
            onClose = {}
        )
    }
}