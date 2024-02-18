package com.bballtending.android.feature.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.login.model.ResponseLogin
import com.bballtending.android.domain.user.repository.UserPreferencesRepository
import com.bballtending.android.feature.intro.model.IntroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val userToken = userPreferencesRepository.getUserToken().getOrElse {
                DLog.e(TAG, it.stackTraceToString())
                ""
            }
            DLog.d(TAG, "userToken=$userToken")

            if (userToken.isNotEmpty()) {
                // TODO: userToken 사용해서 사용자 정보 요청 및 초기화 필요
                _responseAutoLogin.emit(ResponseLogin("TOKEN"))
            } else {
                _responseAutoLogin.emit(null)
            }
        }
    }

    private val _uiState: MutableStateFlow<IntroUiState> = MutableStateFlow(IntroUiState())
    val uiState: StateFlow<IntroUiState> = _uiState.asStateFlow()

    private val _responseAutoLogin: MutableSharedFlow<ResponseLogin?> = MutableSharedFlow()
    val responseAutoLogin: SharedFlow<ResponseLogin?> = _responseAutoLogin.asSharedFlow()

    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}