package com.bballtending.android.feature.addgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.domain.NetworkResult
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import com.bballtending.android.domain.game.usecase.AddPlayerUseCase
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.feature.addgame.model.AddGameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGameViewModel @Inject constructor(
    private val validateGameDataRepository: ValidateGameDataRepository,
    private val addPlayerUseCase: AddPlayerUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddGameUiState> = MutableStateFlow(AddGameUiState())
    val uiState: StateFlow<AddGameUiState> = _uiState.asStateFlow()

    fun onPlayingNowSelect(playingNow: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(playingNow = playingNow)
            }
        }
    }

    fun onGameTypeSelect(gameType: GameType) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(gameType = gameType)
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

            if (validateGameDataRepository.validateQuarter(quarter)) {
                _uiState.update {
                    it.copy(
                        quarter = quarter,
                        quarterMinusEnable = validateGameDataRepository.validateQuarter(quarter - 1),
                        quarterPlusEnable = validateGameDataRepository.validateQuarter(quarter + 1)
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

            if (validateGameDataRepository.validatePlayTime(playTime)) {
                _uiState.update {
                    it.copy(
                        playTime = playTime,
                        playTimeMinusEnable = validateGameDataRepository.validatePlayTime(
                            playTime - 1
                        ),
                        playTimePlusEnable = validateGameDataRepository.validatePlayTime(
                            playTime + 1
                        )
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

            if (validateGameDataRepository.validateBreakTime(breakTime)) {
                _uiState.update {
                    it.copy(
                        breakTime = breakTime,
                        breakTimeMinusEnable = validateGameDataRepository.validateBreakTime(
                            breakTime - 1
                        ),
                        breakTimePlusEnable = validateGameDataRepository.validateBreakTime(
                            breakTime + 1
                        )
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

    companion object {
        private const val TAG: String = "AddGameViewModel"
    }
}