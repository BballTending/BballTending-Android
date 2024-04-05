package com.bballtending.android.feature.addgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.R
import com.bballtending.android.TestModule
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.feature.Border
import com.bballtending.android.feature.addgame.component.AddPlayerCard
import com.bballtending.android.feature.addgame.component.PlayerInfoCard
import com.bballtending.android.feature.addgame.model.AddGameUiState
import com.bballtending.android.feature.border
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

const val ADD_GAME_SCREEN_ROUTE: String = "add_game"

fun NavGraphBuilder.addGameScreen(
    onClose: () -> Unit
) {
    composable(
        route = ADD_GAME_SCREEN_ROUTE
    ) {
        AddGameScreen(onClose)
    }
}

@Composable
private fun AddGameScreen(
    onClose: () -> Unit,
    addGameViewModel: AddGameViewModel = hiltViewModel()
) {
    val uiState: AddGameUiState by addGameViewModel.uiState.collectAsStateWithLifecycle()

    AddGameScreen(
        playingNow = uiState.playingNow,
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
//        homeTeamPlayer = uiState.homeTeamPlayer.toImmutableList(),
        homeTeamPlayer = TestModule.createTestData().homeTeamPlayer.toImmutableList(),
        awayTeamPlayer = uiState.awayTeamPlayer.toImmutableList(),
        onPlayingNowSelect = addGameViewModel::onPlayingNowSelect,
        onGameTypeSelect = addGameViewModel::onGameTypeSelect,
        onQuarterChange = addGameViewModel::onQuarterChange,
        onPlayTimeChange = addGameViewModel::onPlayTimeChange,
        onBreakTimeChange = addGameViewModel::onBreakTimeChange,
        onPlayerAdded = addGameViewModel::onPlayerAdded,
        onPlayerRemoved = addGameViewModel::onPlayerRemoved,
        onClose = onClose
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGameScreen(
    playingNow: Boolean?,
    gameType: GameType?,
    quarter: Int,
    quarterMinusEnable: Boolean,
    quarterPlusEnable: Boolean,
    playTime: Int,
    playTimeMinusEnable: Boolean,
    playTimePlusEnable: Boolean,
    breakTime: Int,
    breakTimeMinusEnable: Boolean,
    breakTimePlusEnable: Boolean,
    homeTeamPlayer: ImmutableList<PlayerData>,
    awayTeamPlayer: ImmutableList<PlayerData>,
    onPlayingNowSelect: (Boolean) -> Unit,
    onGameTypeSelect: (GameType) -> Unit,
    onQuarterChange: (Int) -> Unit,
    onPlayTimeChange: (Int) -> Unit,
    onBreakTimeChange: (Int) -> Unit,
    onPlayerAdded: (Boolean, String, String, Position) -> Unit,
    onPlayerRemoved: (Boolean, String, String, Position) -> Unit,
    onClose: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

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
                                contentDescription = "Close",
                                modifier = Modifier.padding(
                                    start = 15.dp,
                                    top = 13.dp,
                                    end = 15.dp,
                                    bottom = 13.dp
                                )
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
                GameTypeContent(
                    gameType = gameType,
                    onGameTypeSelect = onGameTypeSelect,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                GameTimeContent(
                    quarter = quarter,
                    quarterMinusEnable = quarterMinusEnable,
                    quarterPlusEnable = quarterPlusEnable,
                    playTime = playTime,
                    playTimeMinusEnable = playTimeMinusEnable,
                    playTimePlusEnable = playTimePlusEnable,
                    breakTime = breakTime,
                    breakTimeMinusEnable = breakTimeMinusEnable,
                    breakTimePlusEnable = breakTimePlusEnable,
                    onQuarterChange = onQuarterChange,
                    onPlayTimeChange = onPlayTimeChange,
                    onBreakTimeChange = onBreakTimeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                PlayerInfoContent(
                    isHomeTeam = true,
                    playerList = homeTeamPlayer,
                    onPlayerAdded = onPlayerAdded,
                    onPlayerRemoved = onPlayerRemoved,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
                PlayerInfoContent(
                    isHomeTeam = false,
                    playerList = awayTeamPlayer,
                    onPlayerAdded = onPlayerAdded,
                    onPlayerRemoved = onPlayerRemoved,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .background(color = BballTendingTheme.colors.background)
                )
            }
        }
    }
}

@Composable
private fun PlayingNowContent(
    playingNow: Boolean?,
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
                    .background(color = if (playingNow == true) selectedBgColor else unselectedBgColor)
                    .then(
                        if (playingNow != true)
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
                    style = if (playingNow == true) selectedFontStyle else unselectedFontStyle
                )
            }
            Box(
                modifier = Modifier
                    .noRippleClickable { onPlayingNowSelect(false) }
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .background(color = if (playingNow == false) selectedBgColor else unselectedBgColor)
                    .then(
                        if (playingNow != false)
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
                    style = if (playingNow == false) selectedFontStyle else unselectedFontStyle
                )
            }

        }
    }
}

@Composable
private fun GameTypeContent(
    gameType: GameType?,
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
    quarter: Int,
    quarterMinusEnable: Boolean,
    quarterPlusEnable: Boolean,
    playTime: Int,
    playTimeMinusEnable: Boolean,
    playTimePlusEnable: Boolean,
    breakTime: Int,
    breakTimeMinusEnable: Boolean,
    breakTimePlusEnable: Boolean,
    onQuarterChange: (Int) -> Unit,
    onPlayTimeChange: (Int) -> Unit,
    onBreakTimeChange: (Int) -> Unit,
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
    }
}

@Composable
private fun PlayerInfoContent(
    isHomeTeam: Boolean,
    playerList: ImmutableList<PlayerData>,
    onPlayerAdded: (Boolean, String, String, Position) -> Unit,
    onPlayerRemoved: (Boolean, String, String, Position) -> Unit,
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
                        onAddPlayerCardClick = {

                        }
                    )
                }

                for (playerData in playerList) {
                    item {
                        PlayerInfoCard(
                            isHomeTeam = isHomeTeam,
                            playerData = playerData,
                            onPlayerInfoCardClick = {

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
    BballTendingTheme {
        AddGameScreen(
            onClose = {}
        )
    }
}