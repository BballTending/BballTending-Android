package com.bballtending.android

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bballtending.android.feature.addgame.ADD_GAME_SCREEN_ROUTE
import com.bballtending.android.feature.addgame.addGameScreen
import com.bballtending.android.feature.home.HOME_SCREEN_ROUTE
import com.bballtending.android.feature.home.homeScreen
import com.bballtending.android.feature.playgame.PLAY_GAME_SCREEN_ROUTE
import com.bballtending.android.feature.playgame.playGameScreen
import com.bballtending.android.ui.theme.BballTendingTheme
import com.google.gson.Gson

private const val GAME_ROUTE: String = "game"

@Composable
fun BballTendingApp(
    onFinish: () -> Unit,
    requestPortraitMode: () -> Unit,
    requestLandscapeMode: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    BballTendingTheme {
        NavHost(
            navController = navController,
            startDestination = HOME_SCREEN_ROUTE
        ) {
            homeScreen(
                onFinish = onFinish,
                onGameTypeSelect = { gameType, gameDate ->
                    navController.navigate("$ADD_GAME_SCREEN_ROUTE/${gameType.ordinal}/${gameDate.year}/${gameDate.month}/${gameDate.day}")
                }
            )
            addGameScreen(
                onClose = {
                    navController.popBackStack(
                        route = HOME_SCREEN_ROUTE,
                        inclusive = false,
                        saveState = false
                    )
                },
                onStartGame = { gameData ->
                    requestLandscapeMode()
                    val gameDataJson = Uri.encode(Gson().toJson(gameData))
                    navController.navigate("$PLAY_GAME_SCREEN_ROUTE/$gameDataJson")
                }
            )
            playGameScreen(
                onFinish = {
                    requestPortraitMode()
                    navController.popBackStack(
                        route = HOME_SCREEN_ROUTE,
                        inclusive = false,
                        saveState = false
                    )
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

