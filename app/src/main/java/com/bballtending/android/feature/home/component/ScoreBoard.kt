package com.bballtending.android.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bballtending.android.R
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.OnPrimary
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import com.bballtending.android.ui.theme.WinScoreRed

@Composable
fun ScoreBoard(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    gameData: GameData? = null,
    isOnPrimary: Boolean = false,
    isDetail: Boolean = false
) {
    BballTendingTheme {
        val gameType = when (gameData?.gameType) {
            GameType.FULL_COURT -> "(5X5)"
            GameType.HALF_COURT -> "(3X3)"
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
            val hour = gameData.hour.let {
                if (it > 10) it.toString() else "0$it"
            }
            val minute = gameData.minute.let {
                if (it > 10) it.toString() else "0$it"
            }
            "$hour:$minute"
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreBoard(
                gameTitle = gameTitle,
                homeTeamName = homeTeamName,
                awayTeamName = awayTeamName,
                homeTeamScore = homeTeamScore,
                awayTeamScore = awayTeamScore,
                gameTime = gameTime,
                isOnPrimary = isOnPrimary
            )

            if (isDetail && gameData != null) {
                Spacer(modifier = Modifier.height(12.dp))
                DetailScoreBoard(gameData = gameData, isOnPrimary = isOnPrimary)
            }
        }
    }
}

@Composable
private fun ScoreBoard(
    gameTitle: String,
    homeTeamName: String = "Home",
    awayTeamName: String = "Away",
    homeTeamScore: Int = 0,
    awayTeamScore: Int = 0,
    gameTime: String = "",
    isOnPrimary: Boolean = false
) {
    val homeTeamScoreColor = when {
        isOnPrimary -> Color.White
        homeTeamScore == 0 && awayTeamScore == 0 -> TextHintGray
        homeTeamScore > awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val awayTeamScoreColor = when {
        isOnPrimary -> Color.White
        homeTeamScore == 0 && awayTeamScore == 0 -> TextHintGray
        homeTeamScore < awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val teamNameColor =
        if (isOnPrimary) Color.White else if (homeTeamScore == 0 && awayTeamScore == 0) TextHintGray else TextBlack
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = gameTitle,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = BballTendingTheme.typography.bold.copy(
                color = if (isOnPrimary) Color.White else TextBlack,
                fontSize = 14.sp
            )
        )
        Spacer(Modifier.height(20.dp))

        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (homeTeamIconRef, homeTeamNameRef, scoreLayoutRef, awayTeamLogoRef, awayTeamNameRef) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.icon_home_team),
                contentDescription = "HomeTeam Logo",
                modifier = Modifier.constrainAs(homeTeamIconRef) {
                    end.linkTo(scoreLayoutRef.start, margin = 5.dp)
                    centerVerticallyTo(parent)
                }
            )
            Text(
                text = homeTeamName,
                modifier = Modifier.constrainAs(homeTeamNameRef) {
                    top.linkTo(homeTeamIconRef.bottom, margin = 6.dp)
                    start.linkTo(homeTeamIconRef.start)
                    end.linkTo(homeTeamIconRef.end)
                },
                style = BballTendingTheme.typography.bold.copy(
                    color = teamNameColor,
                    fontSize = 14.sp
                ),
                maxLines = 1
            )

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(scoreLayoutRef) {
                        centerTo(parent)
                    }
            ) {
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
                            color = if (isOnPrimary) Color.White else TextHintGray,
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
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
            }

            Image(
                painter = painterResource(id = R.drawable.icon_away_team),
                contentDescription = "AwayTeam Logo",
                modifier = Modifier.constrainAs(awayTeamLogoRef) {
                    start.linkTo(scoreLayoutRef.end, margin = 5.dp)
                    centerVerticallyTo(parent)
                }
            )
            Text(
                text = awayTeamName,
                modifier = Modifier.constrainAs(awayTeamNameRef) {
                    top.linkTo(awayTeamLogoRef.bottom, margin = 6.dp)
                    start.linkTo(awayTeamLogoRef.start)
                    end.linkTo(awayTeamLogoRef.end)
                },
                style = BballTendingTheme.typography.bold.copy(
                    color = teamNameColor,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
private fun DetailScoreBoard(
    gameData: GameData,
    isOnPrimary: Boolean = false
) {
    BballTendingTheme {
        val headerColor = if (isOnPrimary) OnPrimary else TextBlack
        val commonColor = if (isOnPrimary) Color.White else TextBlack

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row {
                // 팀명
                Box(
                    modifier = Modifier
                        .widthIn(min = teamNameWidth)
                        .heightIn(min = headerHeight)
                ) {
                    Text(
                        text = stringResource(id = R.string.scoreBoard_teamName),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 7.dp, end = 7.dp),
                        style = BballTendingTheme.typography.medium.copy(
                            color = headerColor,
                            fontSize = 10.sp
                        )
                    )
                }
                // 1Q
                QuarterHeaderItem(
                    stringResource(id = R.string.scoreBoard_firstQuarter),
                    headerColor
                )
                // 2Q
                QuarterHeaderItem(
                    stringResource(id = R.string.scoreBoard_secondQuarter),
                    headerColor
                )
                // 3Q
                QuarterHeaderItem(
                    stringResource(id = R.string.scoreBoard_thirdQuarter),
                    headerColor
                )
                // 4Q
                QuarterHeaderItem(
                    stringResource(id = R.string.scoreBoard_fourthQuarter),
                    headerColor
                )
                // TOTAL
                Box(
                    modifier = Modifier
                        .widthIn(min = totalScoreWidth)
                        .heightIn(min = headerHeight)
                ) {
                    Text(
                        text = stringResource(id = R.string.scoreBoard_totalScore),
                        modifier = Modifier.align(Alignment.Center),
                        style = BballTendingTheme.typography.medium.copy(
                            color = headerColor,
                            fontSize = 10.sp
                        )
                    )
                }
            }
            // Home
            Row {
                // 홈 팀명
                Box(
                    modifier = Modifier
                        .widthIn(min = teamNameWidth)
                        .heightIn(min = cellHeight)
                ) {
                    Text(
                        text = gameData.homeTeamName,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 7.dp, end = 7.dp),
                        style = BballTendingTheme.typography.medium.copy(
                            color = commonColor,
                            fontSize = 10.sp
                        )
                    )
                }
                // 1Q
                QuarterCellItem(gameData.homeTeamScoreByQuarter[0].toString(), commonColor)
                // 2Q
                QuarterCellItem(gameData.homeTeamScoreByQuarter[1].toString(), commonColor)
                // 3Q
                QuarterCellItem(gameData.homeTeamScoreByQuarter[2].toString(), commonColor)
                // 4Q
                QuarterCellItem(gameData.homeTeamScoreByQuarter[3].toString(), commonColor)
                // Total
                Box(
                    modifier = Modifier
                        .widthIn(totalScoreWidth)
                        .heightIn(cellHeight)
                ) {
                    Text(
                        text = gameData.homeTeamTotalScore.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        style = BballTendingTheme.typography.medium.copy(
                            color = commonColor,
                            fontSize = 10.sp
                        )
                    )
                }
            }
            // Away
            Row {
                // 홈 팀명
                Box(
                    modifier = Modifier
                        .widthIn(min = teamNameWidth)
                        .heightIn(min = cellHeight)
                ) {
                    Text(
                        text = gameData.awayTeamName,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 7.dp, end = 7.dp),
                        style = BballTendingTheme.typography.medium.copy(
                            color = commonColor,
                            fontSize = 10.sp
                        )
                    )
                }
                // 1Q
                QuarterCellItem(gameData.awayTeamScoreByQuarter[0].toString(), commonColor)
                // 2Q
                QuarterCellItem(gameData.awayTeamScoreByQuarter[1].toString(), commonColor)
                // 3Q
                QuarterCellItem(gameData.awayTeamScoreByQuarter[2].toString(), commonColor)
                // 4Q
                QuarterCellItem(gameData.awayTeamScoreByQuarter[3].toString(), commonColor)
                // Total
                Box(
                    modifier = Modifier
                        .widthIn(totalScoreWidth)
                        .heightIn(cellHeight)
                ) {
                    Text(
                        text = gameData.awayTeamTotalScore.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        style = BballTendingTheme.typography.medium.copy(
                            color = commonColor,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun QuarterHeaderItem(
    text: String,
    color: Color
) {
    BballTendingTheme {
        Box(
            modifier = Modifier
                .widthIn(min = quarterWidth)
                .heightIn(min = headerHeight)
        ) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = BballTendingTheme.typography.medium.copy(color = color, fontSize = 10.sp)
            )
        }
    }
}

@Composable
private fun QuarterCellItem(
    text: String,
    color: Color
) {
    BballTendingTheme {
        Box(
            modifier = Modifier
                .widthIn(min = quarterWidth)
                .heightIn(min = cellHeight)
        ) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = BballTendingTheme.typography.medium.copy(color = color, fontSize = 10.sp)
            )
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

private val headerHeight: Dp = 20.dp
private val cellHeight: Dp = 15.dp
private val teamNameWidth: Dp = 70.dp
private val quarterWidth: Dp = 35.dp
private val totalScoreWidth: Dp = 70.dp