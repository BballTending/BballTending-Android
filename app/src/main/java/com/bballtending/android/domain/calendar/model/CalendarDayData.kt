package com.bballtending.android.domain.calendar.model

import com.bballtending.android.domain.game.model.GameData

data class CalendarDayData(
    val year: Int,
    val month: Int,
    val day: Int,
    val gameRecord: List<GameData>
) {
    val isEmpty: Boolean get() = gameRecord.isEmpty()
}