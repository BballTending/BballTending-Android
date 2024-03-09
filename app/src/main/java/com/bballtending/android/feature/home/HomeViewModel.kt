package com.bballtending.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.game.repository.GameRepository
import com.bballtending.android.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val curLocalDate = LocalDate.now()
            val year = curLocalDate.year
            val month = curLocalDate.monthValue
            val gameMap = gameRepository.requestGameDataWithMonth(year, month)
            _uiState.update {
                it.copy(gameMap = gameMap)
            }
        }
    }

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onSelectedDayChange(year: Int, month: Int, day: Int) {
        DLog.d("${TAG}_onSelectedDayChange", "year=$year, month=$month, day=$day")
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedYear = year,
                    selectedMonth = month,
                    selectedDay = day
                )
            }
        }
    }

    fun onGameTypeSelect(gameType: GameType) {
        
    }

    companion object {
        private const val TAG: String = "HomeViewModel"
    }
}