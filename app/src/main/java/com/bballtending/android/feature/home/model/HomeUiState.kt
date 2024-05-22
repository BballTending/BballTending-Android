package com.bballtending.android.feature.home.model

import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.SortType
import java.time.LocalDate

data class HomeUiState(
    val selectedDate: GameDate = GameDate(
        year = LocalDate.now().year,
        month = LocalDate.now().monthValue,
        day = LocalDate.now().dayOfMonth
    ),
    val selectedDateGameList: List<GameData> = listOf(),
    val gameMap: Map<GameDate, List<GameData>> = hashMapOf(),
    val gameExistDate: Set<GameDate> = setOf(),
    val homeTeamPlayerSortType: SortType = SortType.DEFAULT,
    val awayTeamPlayerSortType: SortType = SortType.DEFAULT
)