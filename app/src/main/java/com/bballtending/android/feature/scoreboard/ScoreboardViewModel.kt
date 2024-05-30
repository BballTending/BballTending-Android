package com.bballtending.android.feature.scoreboard

import androidx.lifecycle.ViewModel
import com.bballtending.android.feature.scoreboard.model.ScoreboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableStateFlow<ScoreboardUiState> =
        MutableStateFlow(ScoreboardUiState())
    val uiState: StateFlow<ScoreboardUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG: String = "ScoreboardViewModel"
    }
}