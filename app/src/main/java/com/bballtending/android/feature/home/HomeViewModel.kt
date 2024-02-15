package com.bballtending.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.domain.calendar.model.CalendarMonthData
import com.bballtending.android.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    init {
        viewModelScope.launch {

        }
    }

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val calendarMonthDataQueue: ArrayDeque<CalendarMonthData> = ArrayDeque()

    companion object {
        private const val TAG: String = "HomeViewModel"
    }
}