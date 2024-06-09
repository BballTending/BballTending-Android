package com.bballtending.android.data.repository

import com.bballtending.android.domain.timer.model.TimerState
import com.bballtending.android.domain.timer.repository.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(

) : TimerRepository {

    private val _state: MutableStateFlow<TimerState> = MutableStateFlow(TimerState.Uninitialized)
    override val state = _state.asStateFlow()

    private var timerJob: Job? = null
    private var timerDuration: Long = 0
    private var prevTimeMills: Long = 0

    private fun initTimerJob(playTime: Int) {
        timerDuration = playTime * 60 * 1000L
        timerJob = createTimerJob(start = CoroutineStart.LAZY)
    }

    private fun createTimerJob(start: CoroutineStart = CoroutineStart.DEFAULT): Job {
        return CoroutineScope(Dispatchers.IO).launch(start = start) {
            prevTimeMills = System.currentTimeMillis()

            while (timerDuration > 0L) {
                yield()
                val delayMills = System.currentTimeMillis() - prevTimeMills
                if (delayMills >= TIMER_TICK) {
                    timerDuration -= delayMills

                    val min = timerDuration.div(1000).div(60).toInt()
                    val sec = timerDuration.div(1000).mod(60)
                    _state.value = TimerState.Running(min, sec)
                    prevTimeMills = System.currentTimeMillis()
                }
            }
        }
    }

    override fun initTimer(playTime: Int) {
        initTimerJob(playTime)
        _state.value = TimerState.Ready
    }

    override fun start(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        val currentState = getCurrentTimerState()
        if (currentState == TimerState.Ready) {
            timerJob?.start()
            onSuccess()
            _state.value = TimerState.Start
        } else {
            onFailure(Throwable("TimerState is $currentState"))
        }
    }

    override fun resume(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        val currentState = getCurrentTimerState()
        if (currentState == TimerState.Pause) {
            timerJob = createTimerJob()
            onSuccess()
            _state.value = TimerState.Resume
        } else {
            onFailure(Throwable("Timer must have been PAUSE State"))
        }
    }

    override fun pause() {
        timerJob?.cancel()
        timerJob = null
        _state.value = TimerState.Pause
    }

    override fun cancel() {
        timerJob?.cancel()
        timerJob = null
        _state.value = TimerState.Finish
    }

    override fun getCurrentTimerState(): TimerState {
        return state.value
    }

    companion object {
        private const val TAG: String = "TimerRepositoryImpl"
        private const val TIMER_TICK: Long = 1000
    }

}