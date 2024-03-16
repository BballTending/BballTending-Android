package com.bballtending.android.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import com.bballtending.android.ui.theme.WinScoreRed

@Composable
fun ScoreBoard(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    gameData: GameData? = null
) {
    BballTendingTheme {
        val gameType = when (gameData?.gameType) {
            GameType.FULL_COURT -> "5X5"
            GameType.HALF_COURT -> "3X3"
            else -> ""
        }
        val gameTitle = stringResource(
            id = R.string.game_title_format,
            selectedYear,
            selectedMonth,
            selectedDay
        ).let {
            if (gameType.isNotEmpty()) "$it $gameType" else it
        }
        val homeTeamName = gameData?.homeTeamName ?: "Home"
        val awayTeamName = gameData?.awayTeamName ?: "Away"
        val homeTeamScore = gameData?.homeTeamTotalScore ?: 0
        val awayTeamScore = gameData?.awayTeamTotalScore ?: 0
        val gameTime = if (gameData == null) {
            ""
        } else {
            stringResource(id = R.string.game_time_format, gameData.hour, gameData.minute)
        }

        ScoreBoard(
            gameTitle = gameTitle,
            homeTeamName = homeTeamName,
            awayTeamName = awayTeamName,
            homeTeamScore = homeTeamScore,
            awayTeamScore = awayTeamScore,
            gameTime = gameTime
        )
    }
}

@Composable
private fun ScoreBoard(
    gameTitle: String,
    homeTeamName: String = "Home",
    awayTeamName: String = "Away",
    homeTeamScore: Int = 0,
    awayTeamScore: Int = 0,
    gameTime: String = ""
) {
    val homeTeamScoreColor = when {
        homeTeamScore == 0 && awayTeamScore == 0 -> TextHintGray
        homeTeamScore > awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val awayTeamScoreColor = when {
        homeTeamScore == 0 && awayTeamScore == 0 -> TextHintGray
        homeTeamScore < awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val teamNameColor = if (homeTeamScore == 0 && awayTeamScore == 0) TextHintGray else TextBlack
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Spacer(Modifier.height(40.dp))
        Text(
            text = gameTitle,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = BballTendingTheme.typography.medium.copy(fontSize = 14.sp)
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_home_team),
                    contentDescription = "HomeTeam Logo"
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = homeTeamName,
                    style = BballTendingTheme.typography.bold.copy(
                        color = teamNameColor,
                        fontSize = 14.sp
                    )
                )
            }
            Text(
                text = homeTeamScore.toString(),
                modifier = Modifier
                    .width(65.dp)
                    .align(Alignment.CenterVertically),
                style = BballTendingTheme.typography.bold.copy(
                    color = homeTeamScoreColor,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.End
            )
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 25.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "vs",
                    style = BballTendingTheme.typography.medium.copy(
                        color = teamNameColor,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = gameTime,
                    modifier = Modifier.padding(top = 8.dp),
                    style = BballTendingTheme.typography.medium.copy(
                        color = TextHintGray,
                        fontSize = 12.sp
                    )
                )
            }
            Text(
                text = awayTeamScore.toString(),
                modifier = Modifier
                    .width(65.dp)
                    .align(Alignment.CenterVertically),
                style = BballTendingTheme.typography.bold.copy(
                    color = awayTeamScoreColor,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Start
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_away_team),
                    contentDescription = "AwayTeam Logo"
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = awayTeamName,
                    style = BballTendingTheme.typography.bold.copy(
                        color = teamNameColor,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun ScoreBoardPreview() {
    BballTendingTheme {
        ScoreBoard(
            gameTitle = "2024년 2월 25일 경기 (5X5)",
            homeTeamScore = 74,
            awayTeamScore = 67,
            gameTime = "09:30"
        )
    }
}