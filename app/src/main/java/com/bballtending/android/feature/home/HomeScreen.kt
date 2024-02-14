package com.bballtending.android.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.feature.home.model.HomeUiState

const val HOME_SCREEN_ROUTE: String = "home"

fun NavGraphBuilder.homeScreen() {
    composable(
        route = HOME_SCREEN_ROUTE
    ) {
        homeScreen()
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState: HomeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()


}