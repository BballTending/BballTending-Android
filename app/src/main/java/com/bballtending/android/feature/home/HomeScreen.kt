package com.bballtending.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.feature.home.component.HorizontalCalendar
import com.bballtending.android.feature.home.model.HomeUiState
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
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
        selectedDay = uiState.selectedDay,
        gameMap = uiState.gameMap,
        onSelectedDayChange = homeViewModel::onSelectedDayChange
    )
}

@Composable
fun HomeScreen(
    selectedDay: Int,
    gameMap: Map<Int, List<GameData>>,
    onSelectedDayChange: (year: Int, month: Int, day: Int) -> Unit
) {
    BballTendingTheme {
        Box(
            modifier = Modifier
                .background(BballTendingTheme.colors.background)
                .fillMaxSize()
        ) {
            HorizontalCalendar(
                gameMap = gameMap.toImmutableMap(),
                onSelectedDayChange = onSelectedDayChange
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