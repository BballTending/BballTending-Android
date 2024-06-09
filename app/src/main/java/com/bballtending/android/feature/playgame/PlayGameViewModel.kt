package com.bballtending.android.feature.playgame

import androidx.lifecycle.viewModelScope
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.timer.model.TimerState
import com.bballtending.android.domain.timer.repository.TimerRepository
import com.bballtending.android.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayGameViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : BaseViewModel<PlayGameContract.Event, PlayGameContract.PlayGameUiState, PlayGameContract.Effect>() {

    init {
        viewModelScope.launch {
            timerRepository.state.collect { timerState ->
                when (timerState) {
                    TimerState.Uninitialized -> {
                        setState { copy(curTimerState = timerState) }
                    }

                    TimerState.Ready -> {
                        setState { copy(curTimerState = timerState) }
                    }

                    TimerState.Start -> {
                        setState { copy(curTimerState = timerState) }
                    }

                    TimerState.Resume -> {
                        setState {
                            copy(
                                curTimerState = timerState
                            )
                        }
                    }

                    is TimerState.Running -> {
                        setState {
                            copy(
                                curPlayTimeUnitMin = timerState.min,
                                curPlayTimeUnitSec = timerState.sec,
                                curTimerState = timerState
                            )
                        }
                    }

                    TimerState.Pause -> {
                        setState {
                            copy(
                                curTimerState = timerState
                            )
                        }
                    }

                    TimerState.Finish -> {
                        setState {
                            copy(
                                curPlayTimeUnitMin = 0,
                                curPlayTimeUnitSec = 0,
                                curTimerState = timerState
                            )
                        }
                    }
                }
            }
        }
    }

    private lateinit var initGameData: GameData
    private lateinit var homeTeamScoreByQuarter: IntArray
    private lateinit var awayTeamScoreByQuarter: IntArray

    override fun createInitialState(): PlayGameContract.PlayGameUiState {
        return PlayGameContract.PlayGameUiState()
    }

    override fun handleEvent(event: PlayGameContract.Event) {
        when (event) {
            is PlayGameContract.Event.OnInitGameData -> {
                val gameData = event.gameData
                initGameData = gameData
                homeTeamScoreByQuarter = IntArray(gameData.quarter) { 0 }
                awayTeamScoreByQuarter = IntArray(gameData.quarter) { 0 }

                setState {
                    copy(
                        curPlayTimeUnitMin = gameData.playTime,
                        curPlayTimeUnitSec = 0,
                        curQuarter = 1,
                        maxQuarter = gameData.quarter,
                        homeTeamPlayer = gameData.homeTeamPlayer,
                        awayTeamPlayer = gameData.awayTeamPlayer
                    )
                }
                timerRepository.initTimer(gameData.playTime)
            }

            PlayGameContract.Event.OnBackButtonClicked -> {
                setState { copy(dialogState = PlayGameContract.DialogState.BACK_BUTTON_DIALOG) }
            }

            PlayGameContract.Event.OnBackButtonDialogDismiss -> {
                setState { copy(dialogState = PlayGameContract.DialogState.NONE) }
            }

            PlayGameContract.Event.OnGameStart -> {
                timerRepository.start(
                    onSuccess = {
                        DLog.d(TAG, "Start Timer")
                    },
                    onFailure = {
                        // TODO: Error Toast Message
                    }
                )
            }

            PlayGameContract.Event.OnGamePause -> {
                timerRepository.pause()
            }

            PlayGameContract.Event.OnGameResume -> {
                timerRepository.resume(
                    onSuccess = {
                        DLog.d(TAG, "Resume Timer")
                    },
                    onFailure = {
                        // TODO: Error Toast Message
                    }
                )
            }

            PlayGameContract.Event.OnGameCancel -> {
                timerRepository.cancel()
            }

            PlayGameContract.Event.OnQuarterFinish -> TODO()
            PlayGameContract.Event.OnNextQuarter -> TODO()
            is PlayGameContract.Event.OnPlayerCardClicked -> TODO()
            is PlayGameContract.Event.OnTwoPointCardClicked -> TODO()
            is PlayGameContract.Event.OnThreePointCardClicked -> TODO()
            is PlayGameContract.Event.OnReboundCardClicked -> TODO()
            is PlayGameContract.Event.OnStealCardClicked -> TODO()
            is PlayGameContract.Event.OnAssistCardClicked -> TODO()
            is PlayGameContract.Event.OnBlockCardClicked -> TODO()
            is PlayGameContract.Event.OnFoulCardClicked -> TODO()
            is PlayGameContract.Event.OnTurnoverCardClicked -> TODO()
        }
    }

    companion object {
        private const val TAG: String = "ScoreboardViewModel"
    }

}