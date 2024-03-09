package com.bballtending.android.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.R
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.feature.dialog.GameTypeDialog
import com.bballtending.android.feature.home.component.DraggableBottomSheet
import com.bballtending.android.feature.home.component.HorizontalCalendar
import com.bballtending.android.feature.home.component.ScoreBoard
import com.bballtending.android.feature.home.model.HomeUiState
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.toImmutableMap

const val HOME_SCREEN_ROUTE: String = "home"

fun NavGraphBuilder.homeScreen() {
    composable(
        route = HOME_SCREEN_ROUTE
    ) {
        HomeScreen()
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        selectedYear = uiState.selectedYear,
        selectedMonth = uiState.selectedMonth,
        selectedDay = uiState.selectedDay,
        gameMap = uiState.gameMap,
        onSelectedDayChange = homeViewModel::onSelectedDayChange,
        onGameTypeSelect = homeViewModel::onGameTypeSelect
    )
}

@Composable
fun HomeScreen(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    gameMap: Map<Int, List<GameData>>,
    onSelectedDayChange: (year: Int, month: Int, day: Int) -> Unit,
    onGameTypeSelect: (gameType: GameType) -> Unit
) {
    val gameData = gameMap[selectedDay]
    var gameTypeDialogVisible by remember { mutableStateOf(false) }

    BballTendingTheme {
        ConstraintLayout(
            modifier = Modifier
                .background(BballTendingTheme.colors.background)
                .fillMaxSize()
        ) {
            val (calendar, chartBtn, bottomScreen, playGameBtn) = createRefs()
            HorizontalCalendar(
                gameMap = gameMap.toImmutableMap(),
                onSelectedDayChange = onSelectedDayChange,
                modifier = Modifier
                    .constrainAs(calendar) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }
            )

            Button(
                onClick = {

                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 3.dp, end = 20.dp, bottom = 15.dp)
                    .constrainAs(chartBtn) {
                        top.linkTo(calendar.bottom)
                        end.linkTo(parent.end)
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

            DraggableBottomSheet(
                modifier = Modifier
                    .constrainAs(bottomScreen) {
                        top.linkTo(chartBtn.bottom)
                        centerHorizontallyTo(parent)
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ScoreBoard(
                        selectedYear = selectedYear,
                        selectedMonth = selectedMonth,
                        selectedDay = selectedDay,
                        gameData = gameData?.firstOrNull()
                    )

                    // 게임 기록이 없는 경우
                    if (gameData == null) {
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
                    // 게임 기록이 있는 경우
                    else {

                    }
                }
            }

            if (gameData == null) {
                Button(
                    onClick = {
                        gameTypeDialogVisible = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 80.dp, end = 80.dp, bottom = 30.dp)
                        .constrainAs(playGameBtn) {
                            bottom.linkTo(parent.bottom)
                            centerHorizontallyTo(parent)
                        },
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
                        text = stringResource(id = R.string.home_play_game),
                        style = BballTendingTheme.typography.medium.copy(
                            color = Color.White,
                            fontSize = 14.sp
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
                    onGameTypeSelect(gameType)
                }
            )
        }
    }
}

@DevicePreview
@Composable
fun HomeScreenPreview() {
    BballTendingTheme {
        HomeScreen()
    }
}