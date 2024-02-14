package com.bballtending.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bballtending.android.feature.intro.INTRO_SCREEN_ROUTE
import com.bballtending.android.feature.intro.introScreen
import com.bballtending.android.ui.theme.BballTendingTheme

@Composable
fun BballTendingApp(
    navController: NavHostController = rememberNavController()
) {
    BballTendingTheme {
        NavHost(
            navController = navController,
            startDestination = INTRO_SCREEN_ROUTE
        ) {
            introScreen(
                onComplete = {

                },
                onLogin = {
                    
                }
            )
        }
    }
}