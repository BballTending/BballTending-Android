package com.bballtending.android.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.R
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.game.model.SortType
import com.bballtending.android.feature.dialog.GameTypeDialog
import com.bballtending.android.feature.home.component.CircleIndicator
import com.bballtending.android.feature.home.component.HorizontalCalendar
import com.bballtending.android.feature.home.component.ScoreBoard
import com.bballtending.android.feature.home.component.ScoreTable
import com.bballtending.android.feature.home.model.HomeUiState
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

const val HOME_SCREEN_ROUTE: String = "home"

fun NavGraphBuilder.homeScreen(
    onGameTypeSelect: (GameType, GameDate) -> Unit
) {
    composable(
        route = HOME_SCREEN_ROUTE
    ) {
        HomeScreen(onGameTypeSelect)
    }
}

@Composable
private fun HomeScreen(
    onGameTypeSelect: (GameType, GameDate) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        selectedDate = uiState.selectedDate,
        selectedDateGameList = uiState.selectedDateGameList.toImmutableList(),
        gameExistDate = uiState.gameExistDate.toImmutableSet(),
        homeTeamPlayerSortType = uiState.homeTeamPlayerSortType,
        awayTeamPlayerSortType = uiState.awayTeamPlayerSortType,
        onDateChange = homeViewModel::onDateChange,
        onSortTypeChange = homeViewModel::onSortTypeChange,
        onGameTypeSelect = onGameTypeSelect
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    selectedDate: GameDate,
    selectedDateGameList: ImmutableList<GameData>,
    gameExistDate: ImmutableSet<GameDate>,
    homeTeamPlayerSortType: SortType,
    awayTeamPlayerSortType: SortType,
    onDateChange: (GameDate) -> Unit,
    onSortTypeChange: (sortType: SortType, isHomeTeam: Boolean) -> Unit,
    onGameTypeSelect: (GameType, GameDate) -> Unit
) {
    var gameTypeDialogVisible by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState().apply {
        bottomSheetState
    }

    var isExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = scaffoldState.bottomSheetState.currentValue) {
        val currentValue = scaffoldState.bottomSheetState.currentValue
        isExpanded = currentValue == SheetValue.Expanded
    }

    val screenHeightDp = with(LocalConfiguration.current) {
        screenHeightDp
    }
    var peekInitY by remember { mutableStateOf(0) }

    BballTendingTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background)
        ) {
            BottomSheetScaffold(
                sheetContent = {
                    if (isExpanded) {
                        HomeScreenExpandedSheetContent(
                            selectedDate = selectedDate,
                            gameData = selectedDateGameList,
                            homeTeamPlayerSortType = homeTeamPlayerSortType,
                            awayTeamPlayerSortType = awayTeamPlayerSortType,
                            onSortTypeChange = onSortTypeChange
                        )
                    } else {
                        HomeScreenPartiallyExpandedSheetContent(
                            gameDate = selectedDate,
                            gameData = selectedDateGameList,
                            homeTeamPlayerSortType = homeTeamPlayerSortType,
                            awayTeamPlayerSortType = awayTeamPlayerSortType,
                            onSortTypeChange = onSortTypeChange
                        )
                    }
                },
                scaffoldState = scaffoldState,
                sheetPeekHeight = if (peekInitY == 0) {
                    BottomSheetDefaults.SheetPeekHeight
                } else {
                    (screenHeightDp - with(LocalDensity.current) { peekInitY / density }).dp
                },
                sheetDragHandle = null,
                sheetSwipeEnabled = selectedDateGameList.isNotEmpty(),
                sheetContainerColor = BballTendingTheme.colors.background,
                sheetShadowElevation = 5.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BballTendingTheme.colors.background)
                ) {
                    Column {
                        HorizontalCalendar(
                            gameExistDate = gameExistDate,
                            onDateChange = onDateChange
                        )

                        var initFlag = 0
                        Button(
                            onClick = {

                            },
                            modifier = Modifier
                                .padding(top = 3.dp, end = 20.dp, bottom = 15.dp)
                                .wrapContentSize()
                                .align(Alignment.End)
                                .onGloballyPositioned {
                                    if (initFlag == 0) {
                                        initFlag += 1
                                        val position = it.positionInParent()
                                        DLog.d(HOME_SCREEN_ROUTE, "position=$position")
                                        peekInitY = position.y.toInt() + it.size.height + 15
                                    }
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = BballTendingTheme.colors.primary
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 1.dp
                            ),
                            border = BorderStroke(1.dp, BballTendingTheme.colors.primary),
                            contentPadding = PaddingValues(
                                start = 15.dp,
                                top = 9.dp,
                                end = 15.dp,
                                bottom = 7.dp
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.home_chart_btn),
                                style = BballTendingTheme.typography.medium.copy(
                                    color = BballTendingTheme.colors.primary,
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Image(
                                painter = painterResource(id = R.drawable.icon_chart),
                                contentDescription = "차트"
                            )
                        }
                    }
                }
            }

            if (selectedDateGameList.isEmpty()) {
                Button(
                    onClick = {
                        gameTypeDialogVisible = true
                    },
                    modifier = Modifier
                        .padding(start = 80.dp, end = 80.dp, bottom = 30.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BballTendingTheme.colors.primary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 1.dp
                    ),
                    border = BorderStroke(1.dp, BballTendingTheme.colors.primary),
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        top = 12.dp,
                        end = 0.dp,
                        bottom = 11.dp
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.msg_play_game),
                        style = BballTendingTheme.typography.medium.copy(
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    )
                }
            }
            if (isExpanded) {
                Button(
                    onClick = {
                        gameTypeDialogVisible = true
                    },
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .wrapContentSize()
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = BballTendingTheme.colors.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 1.dp
                    ),
                    border = BorderStroke(1.dp, BballTendingTheme.colors.primary),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 0.dp,
                        end = 12.dp,
                        bottom = 0.dp
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add),
                        contentDescription = "Add",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(id = R.string.msg_add_game),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.medium.copy(
                            color = BballTendingTheme.colors.primary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        if (gameTypeDialogVisible) {
            GameTypeDialog(
                onDismissRequest = {
                    gameTypeDialogVisible = false
                },
                onGameTypeSelect = { gameType ->
                    gameTypeDialogVisible = false
                    onGameTypeSelect(gameType, selectedDate)
                }
            )
        }
    }
}

@Composable
private fun HomeScreenPartiallyExpandedSheetContent(
    gameDate: GameDate,
    gameData: ImmutableList<GameData>,
    homeTeamPlayerSortType: SortType,
    awayTeamPlayerSortType: SortType,
    onSortTypeChange: (sortType: SortType, isHomeTeam: Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .padding(top = 15.dp)
                .width(35.dp)
                .height(5.dp)
                .background(color = BorderGray, shape = RoundedCornerShape(10.dp))
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(25.dp))

        ScoreBoard(
            year = gameDate.year,
            month = gameDate.month,
            day = gameDate.day,
            gameData = gameData.firstOrNull()
        )

        // 게임 기록이 없는 경우
        if (gameData.isEmpty()) {
            NoGameInfo(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        // 게임 기록이 있는 경우
        else {
            GameScoreTable(
                gameData.first(),
                homeTeamPlayerSortType = homeTeamPlayerSortType,
                awayTeamPlayerSortType = awayTeamPlayerSortType,
                onSortTypeChange = onSortTypeChange
            )
        }
    }
}

@Composable
private fun HomeScreenExpandedSheetContent(
    selectedDate: GameDate,
    gameData: ImmutableList<GameData>,
    homeTeamPlayerSortType: SortType,
    awayTeamPlayerSortType: SortType,
    onSortTypeChange: (sortType: SortType, isHomeTeam: Boolean) -> Unit
) {
    var curGameIdx by remember { mutableIntStateOf(0) }

    // 게임 기록이 없는 경우
    if (gameData.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .width(35.dp)
                    .height(5.dp)
                    .background(color = BorderGray, shape = RoundedCornerShape(10.dp))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(25.dp))
            ScoreBoard(
                year = selectedDate.year,
                month = selectedDate.month,
                day = selectedDate.day,
                gameData = null
            )
            NoGameInfo(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    // 게임 기록이 있는 경우
    else {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = BballTendingTheme.colors.primary)
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .width(35.dp)
                            .height(5.dp)
                            .background(color = BorderGray, shape = RoundedCornerShape(10.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    ScoreBoard(
                        year = selectedDate.year,
                        month = selectedDate.month,
                        day = selectedDate.day,
                        gameData = gameData[curGameIdx],
                        isOnPrimary = true,
                        isDetail = true
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    CircleIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        curIdx = curGameIdx,
                        max = gameData.size
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (curGameIdx > 0) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_left_arrow_active),
                        contentDescription = "왼쪽 화살표",
                        modifier = Modifier.noRippleClickable {
                            curGameIdx -= 1
                        },
                        tint = Color.White
                    )
                }
                if (curGameIdx < gameData.size - 1) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_right_arrow_active),
                        contentDescription = "왼쪽 화살표",
                        modifier = Modifier.noRippleClickable {
                            curGameIdx + 1
                        },
                        tint = Color.White
                    )
                }
            }
            GameScoreTable(
                gameData.first(),
                homeTeamPlayerSortType = homeTeamPlayerSortType,
                awayTeamPlayerSortType = awayTeamPlayerSortType,
                onSortTypeChange = onSortTypeChange
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun GameScoreTable(
    gameData: GameData,
    homeTeamPlayerSortType: SortType,
    awayTeamPlayerSortType: SortType,
    modifier: Modifier = Modifier,
    onSortTypeChange: (sortType: SortType, isHomeTeam: Boolean) -> Unit
) {
    BballTendingTheme {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.padding(start = 15.dp, top = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_home_team),
                    contentDescription = "홈 팀 로고",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = gameData.homeTeamName,
                    modifier = Modifier.padding(start = 7.dp),
                    style = BballTendingTheme.typography.bold.copy(
                        color = TextBlack,
                        fontSize = 18.sp
                    )
                )
            }
            ScoreTable(
                playerDataList = gameData.homeTeamPlayer.toImmutableList(),
                sortType = homeTeamPlayerSortType,
                modifier = Modifier.padding(top = 10.dp),
                onSortTypeChange = { sortType ->
                    onSortTypeChange(sortType, true)
                }
            )

            Row(
                modifier = Modifier.padding(start = 15.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_away_team),
                    contentDescription = "어웨이 팀 로고",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = gameData.awayTeamName,
                    modifier = Modifier.padding(start = 7.dp),
                    style = BballTendingTheme.typography.bold.copy(
                        color = TextBlack,
                        fontSize = 18.sp
                    )
                )
            }
            ScoreTable(
                playerDataList = gameData.awayTeamPlayer.toImmutableList(),
                sortType = awayTeamPlayerSortType,
                modifier = Modifier.padding(top = 10.dp),
                onSortTypeChange = { sortType ->
                    onSortTypeChange(sortType, false)
                }
            )
        }
    }
}

@Composable
private fun NoGameInfo(
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Column(modifier = modifier) {
            Spacer(Modifier.height(25.dp))
            Text(
                text = stringResource(id = R.string.home_no_game_record),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                style = BballTendingTheme.typography.medium.copy(
                    color = TextHintGray,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@DevicePreview
@Composable
private fun HomeScreenPreview() {
    BballTendingTheme {
        HomeScreen(
            onGameTypeSelect = { _, _ -> }
        )
    }
}