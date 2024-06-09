package com.bballtending.android.domain.timer.model

sealed class TimerState {
    /**
     * 초기 값이 설정되지 않은 상태
     */
    object Uninitialized : TimerState()

    /**
     * 초기 값이 설정이 완료되어 시작 가능한 상태
     */
    object Ready : TimerState()

    /**
     * 타이머가 시작한 상태
     */
    object Start : TimerState()

    /**
     * 타이머가 재개된 상태
     */
    object Resume : TimerState()

    /**
     * 타이머가 실행 중인 상태
     */
    data class Running(val min: Int, val sec: Int) : TimerState()

    /**
     * 타이머가 일시 정지된 상태
     */
    object Pause : TimerState()

    /**
     * 타이머가 종료된 상태
     */
    object Finish : TimerState()
}