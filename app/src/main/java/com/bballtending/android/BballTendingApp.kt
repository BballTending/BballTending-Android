package com.bballtending.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bballtending.android.feature.addgame.ADD_GAME_SCREEN_ROUTE
import com.bballtending.android.feature.addgame.addGameScreen
import com.bballtending.android.feature.home.HOME_SCREEN_ROUTE
import com.bballtending.android.feature.home.homeScreen
import com.bballtending.android.ui.theme.BballTendingTheme

@Composable
fun BballTendingApp(
    navController: NavHostController = rememberNavController()
) {
    BballTendingTheme {
        NavHost(
            navController = navController,
            startDestination = HOME_SCREEN_ROUTE
        ) {
            homeScreen(
                onGameTypeSelect = { gameType ->
                    navController.navigate(ADD_GAME_SCREEN_ROUTE)
                }
            )
            addGameScreen(
                onClose = {
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