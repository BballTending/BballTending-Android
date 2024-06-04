package com.bballtending.android.feature.playgame

import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.feature.base.UiEffect
import com.bballtending.android.feature.base.UiEvent
import com.bballtending.android.feature.base.UiState

class PlayGameContract {

    // Events that user performed
    sealed class Event : UiEvent {
        /**
         * UiState 초기화
         */
        data class OnInitGameData(val gameData: GameData) : Event()

        /**
         * 게임 시작
         */
        object OnGameStart : Event()

        /**
         * 게임 일시 정지
         */
        object OnGamePause : Event()

        /**
         * 게임 재개
         */
        object OnGameResume : Event()

        /**
         * 현재 쿼터 종료
         */
        object OnQuarterFinish : Event()

        /**
         * 다음 쿼터 진행
         */
        object OnNextQuarter : Event()

        /**
         * 게임 종료
         */
        object OnGameFinish : Event()

        /**
         * 선수 카드 클릭 이벤트
         */
        data class OnPlayerCardClicked(val playerData: PlayerData) : Event()

        /**
         * 2점 슛 성공
         */
        data class OnTwoPointCardClicked(val playerData: PlayerData) : Event()

        /**
         * 3점 슛 성공
         */
        data class OnThreePointCardClicked(val playerData: PlayerData) : Event()

        /**
         * 리바운드 성공
         */
        data class OnReboundCardClicked(val playerData: PlayerData) : Event()

        /**
         * 스틸 성공
         */
        data class OnStealCardClicked(val playerData: PlayerData) : Event()

        /**
         * 어시스트 성공
         */
        data class OnAssistCardClicked(val playerData: PlayerData) : Event()

        /**
         * 블록 성공
         */
        data class OnBlockCardClicked(val playerData: PlayerData) : Event()

        /**
         * 파울
         */
        data class OnFoulCardClicked(val playerData: PlayerData) : Event()

        /**
         * 턴오버
         */
        data class OnTurnoverCardClicked(val playerData: PlayerData) : Event()
    }

    // Ui View States
    data class PlayGameUiState(
        val playTimeUnitSec: Int = 0,
        val curQuarter: Int = 1,
        val homeTeamScore: Int = 0,
        val awayTeamScore: Int = 0
    ) : UiState

    sealed class Effect : UiEffect {
        object ShowToast : Effect()
    }

}