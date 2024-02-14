package com.bballtending.android.feature.intro

import androidx.lifecycle.ViewModel
import com.bballtending.android.feature.intro.model.IntroUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class IntroViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableStateFlow<IntroUiState> = MutableStateFlow(IntroUiState())
    val uiState: StateFlow<IntroUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}