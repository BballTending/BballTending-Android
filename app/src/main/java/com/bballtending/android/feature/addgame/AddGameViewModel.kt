package com.bballtending.android.feature.addgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.NetworkResult
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.usecase.AddPlayerUseCase
import com.bballtending.android.domain.game.usecase.ChangeBreakTimeUseCase
import com.bballtending.android.domain.game.usecase.ChangePlayTimeUseCase
import com.bballtending.android.domain.game.usecase.ChangeQuarterUseCase
import com.bballtending.android.domain.game.usecase.ChangeTargetScoreUseCase
import com.bballtending.android.domain.game.usecase.CreateGameUseCase
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.feature.addgame.model.AddGameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val changeQuarterUseCase: ChangeQuarterUseCase,
    private val changePlayTimeUseCase: ChangePlayTimeUseCase,
    private val changeBreakTimeUseCase: ChangeBreakTimeUseCase,
    private val changeTargetScoreUseCase: ChangeTargetScoreUseCase,
    private val addPlayerUseCase: AddPlayerUseCase,
    private val createGameUseCase: CreateGameUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddGameUiState> = MutableStateFlow(AddGameUiState())
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()

    private val _createdGameData: MutableStateFlow<GameData?> = MutableStateFlow(null)
    val createdGameData: StateFlow<GameData?> = _createdGameData.asStateFlow()

    private var gameDate: GameDate = GameDate(0, 0, 0)

    fun initData(
        gameType: GameType,
        gameDate: GameDate
    ) {
        val localDateTime = LocalDateTime.now()
        val playingNow = localDateTime.run {
            (gameDate.year == year) && (gameDate.month == monthValue) && (gameDate.day == dayOfMonth)
        }
        this.gameDate = gameDate
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    playingNow = playingNow,
                    hour = localDateTime.hour,
                    minute = localDateTime.minute
                ).withGameType(gameType)
            }
        }
    }

    fun onPlayingNowSelect(playingNow: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(playingNow = playingNow)
            }
        }
    }

    fun onHourChanged(hour: Int) {
        DLog.d(TAG, "hour=$hour")
        viewModelScope.launch {
            _uiState.update {
                it.copy(hour = hour)
            }
        }
    }

    fun onMinuteChanged(minute: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(minute = minute)
            }
        }
    }

    fun onGameTypeSelect(gameType: GameType) {
        viewModelScope.launch {
            _uiState.update {
                it.copy().withGameType(gameType)
            }
        }
    }

    fun onQuarterChange(sign: Int) {
        viewModelScope.launch {
            val prevQuarter = uiState.value.quarter
            val quarter = if (sign > 0) {
                prevQuarter + 1
            } else if (sign < 0) {
                prevQuarter - 1
            } else {
                return@launch
            }

            if (changeQuarterUseCase(quarter)) {
                _uiState.update {
                    it.copy(
                        quarter = quarter,
                        quarterMinusEnable = changeQuarterUseCase(quarter - 1),
                        quarterPlusEnable = changeQuarterUseCase(quarter + 1)
                    )
                }
            }
        }
    }

    fun onPlayTimeChange(sign: Int) {
        viewModelScope.launch {
            val prevPlayTime = uiState.value.playTime
            val playTime = if (sign > 0) {
                prevPlayTime + 1
            } else if (sign < 0) {
                prevPlayTime - 1
            } else {
                return@launch
            }

            if (changePlayTimeUseCase(playTime)) {
                _uiState.update {
                    it.copy(
                        playTime = playTime,
                        playTimeMinusEnable = changePlayTimeUseCase(playTime - 1),
                        playTimePlusEnable = changePlayTimeUseCase(playTime + 1)
                    )
                }
            }
        }
    }

    fun onBreakTimeChange(sign: Int) {
        viewModelScope.launch {
            val prevBreakTime = uiState.value.breakTime
            val breakTime = if (sign > 0) {
                prevBreakTime + 1
            } else if (sign < 0) {
                prevBreakTime - 1
            } else {
                return@launch
            }

            if (changeBreakTimeUseCase(breakTime)) {
                _uiState.update {
                    it.copy(
                        breakTime = breakTime,
                        breakTimeMinusEnable = changeBreakTimeUseCase(breakTime - 1),
                        breakTimePlusEnable = changeBreakTimeUseCase(breakTime + 1)
                    )
                }
            }
        }
    }

    fun onTargetScoreChange(sign: Int) {
        viewModelScope.launch {
            val prevTargetScore = uiState.value.targetScore
            val targetScore = if (sign > 0) {
                prevTargetScore + 1
            } else if (sign < 0) {
                prevTargetScore - 1
            } else {
                return@launch
            }

            if (changeTargetScoreUseCase(targetScore)) {
                _uiState.update {
                    it.copy(
                        targetScore = targetScore,
                        targetScoreMinusEnable = changeTargetScoreUseCase(targetScore - 1),
                        targetScorePlusEnable = changeTargetScoreUseCase(targetScore + 1)
                    )
                }
            }
        }
    }

    fun onPlayerAdded(
        isHomeTeamPlayer: Boolean,
        name: String,
        number: String,
        position: Position
    ): Boolean {
        val teamPlayerList =
            if (isHomeTeamPlayer) uiState.value.homeTeamPlayer else uiState.value.awayTeamPlayer
        val sameNumberPlayer = teamPlayerList.find { it.number == number }

        // 팀 내에 등번호가 같은 선수가 없는 경우
        return if (sameNumberPlayer == null) {
            viewModelScope.launch {
                val addedPlayerData = addPlayerUseCase(name, number, position)
                if (addedPlayerData is NetworkResult.Success) {
                    val newList = teamPlayerList.toMutableList().apply {
                        add(addedPlayerData.data)
                    }
                    _uiState.update {
                        if (isHomeTeamPlayer)
                            it.copy(homeTeamPlayer = newList)
                        else
                            it.copy(awayTeamPlayer = newList)
                    }
                } else {
                    // TODO: 예외 처리 필요?
                }
            }
            true
        }
        // 팀 내에 등번호가 같은 선수가 있는 경우
        else {
            false
        }
    }

    fun onPlayerModified(isHomeTeamPlayer: Boolean, playerData: PlayerData): Boolean {
        return false
    }

    fun onPlayerRemoved(isHomeTeamPlayer: Boolean, playerData: PlayerData) {
        viewModelScope.launch {
            val teamPlayerList =
                if (isHomeTeamPlayer) uiState.value.homeTeamPlayer else uiState.value.awayTeamPlayer
            val newList = teamPlayerList.toMutableList().apply {
                remove(playerData)
            }
            _uiState.update {
                if (isHomeTeamPlayer)
                    it.copy(homeTeamPlayer = newList)
                else
                    it.copy(awayTeamPlayer = newList)
            }
        }
    }

    fun onStartGame() {
        viewModelScope.launch {
            val uiState = uiState.value
            val gameDate = gameDate
            val gameData = createGameUseCase(
                uiState.gameType,
                gameDate.year,
                gameDate.month,
                gameDate.day,
                uiState.hour,
                uiState.minute,
                uiState.quarter,
                uiState.playTime,
                uiState.breakTime,
                uiState.homeTeamPlayer,
                uiState.awayTeamPlayer
            )
            if (gameData is NetworkResult.Success) {
                DLog.d(TAG, "gameData=$gameData")
                _createdGameData.emit(gameData.data)
            } else {
                // TODO: 예외 처리 필요?
            }
        }
    }

    companion object {
        private const val TAG: String = "AddGameViewModel"
    }
}