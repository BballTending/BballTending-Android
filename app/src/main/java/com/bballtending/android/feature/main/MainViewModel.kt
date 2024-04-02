package com.bballtending.android.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.user.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
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
                // TODO: 사용자 정보 요청 및 초기화 필요
            } else {
                // TODO: 로그인 필요~!
            }
            _splashScreenCondition.emit(false)
        }
    }

    private val _splashScreenCondition: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val splashScreenCondition: StateFlow<Boolean> = _splashScreenCondition.asStateFlow()

    companion object {
        private const val TAG: String = "MainViewModel"
    }

}