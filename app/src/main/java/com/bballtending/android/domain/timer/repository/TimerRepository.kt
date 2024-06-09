package com.bballtending.android.domain.timer.repository

import com.bballtending.android.domain.timer.model.TimerState
import kotlinx.coroutines.flow.StateFlow

interface TimerRepository {

    val state: StateFlow<TimerState>

    fun initTimer(playTime: Int)

    fun start(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit)

    fun resume(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit)

    fun pause()

    fun cancel()

    fun getCurrentTimerState(): TimerState
}