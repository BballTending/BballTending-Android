package com.bballtending.android.feature.home.model

import com.bballtending.android.domain.game.model.GameData
import java.time.LocalDate

data class HomeUiState(
    val localDate: LocalDate = LocalDate.now(),
    val selectedDay: Int = LocalDate.now().dayOfMonth,
    val gameMap: Map<Int, List<GameData>> = hashMapOf()
)