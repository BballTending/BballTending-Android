package com.bballtending.android.feature.playgame

import com.bballtending.android.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayGameViewModel @Inject constructor(

) : BaseViewModel<PlayGameContract.Event, PlayGameContract.PlayGameUiState, PlayGameContract.Effect>() {

    override fun createInitialState(): PlayGameContract.PlayGameUiState {
        return PlayGameContract.PlayGameUiState()
    }

    override fun handleEvent(event: PlayGameContract.Event) {
        when (event) {
            is PlayGameContract.Event.OnInitGameData -> {

            }

            PlayGameContract.Event.OnGameStart -> TODO()
            PlayGameContract.Event.OnGamePause -> TODO()
            PlayGameContract.Event.OnGameResume -> TODO()
            PlayGameContract.Event.OnGameFinish -> TODO()
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