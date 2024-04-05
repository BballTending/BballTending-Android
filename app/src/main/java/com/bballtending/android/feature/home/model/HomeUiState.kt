package com.bballtending.android.feature.home.model

import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: GameDate = GameDate(
        year = LocalDate.now().year,
        LocalDate.now().monthValue,
        LocalDate.now().dayOfMonth
    ),
    val selectedDateGameList: List<GameData> = listOf(),
    val gameMap: Map<GameDate, List<GameData>> = hashMapOf(),
    val gameExistDate: Set<GameDate> = setOf()
)