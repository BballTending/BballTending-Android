package com.bballtending.android.domain.game.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.bballtending.android.domain.player.model.PlayerData
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameData(
    val gameId: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val gameType: GameType,
    val quarter: Int,
    val playTime: Int,
    val breakTime: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamTotalScore: Int,
    val awayTeamTotalScore: Int,
    val homeTeamScoreByQuarter: List<Int>,
    val awayTeamScoreByQuarter: List<Int>,
    val homeTeamPlayer: List<PlayerData>,
    val awayTeamPlayer: List<PlayerData>
) : Parcelable

class GameDataParamType : NavType<GameData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): GameData? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, GameData::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): GameData {
        return Gson().fromJson(value, GameData::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: GameData) {
        bundle.putParcelable(key, value)
    }
}