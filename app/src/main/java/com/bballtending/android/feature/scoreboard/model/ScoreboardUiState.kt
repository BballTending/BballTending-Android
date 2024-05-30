package com.bballtending.android.feature.scoreboard.model

data class ScoreboardUiState(
    val playTimeUnitSec: Int = 0,
    val curQuarter: Int = 1,
    val homeTeamScore: Int = 0,
    val awayTeamScore: Int = 0
)
