package com.bballtending.android.feature.playgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bballtending.android.R
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import com.bballtending.android.ui.theme.WinScoreRed

@Composable
fun PlayGameScoreBoard(
    isHomeTeamLeft: Boolean = true,
    homeTeamName: String = "Home",
    awayTeamName: String = "Away",
    homeTeamScore: Int = 52,
    awayTeamScore: Int = 33,
    gameTime: String = "",
    modifier: Modifier = Modifier
) {
    val homeTeamScoreColor = when {
        homeTeamScore > awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val awayTeamScoreColor = when {
        homeTeamScore < awayTeamScore -> WinScoreRed
        else -> TextBlack
    }
    val teamNameColor = TextBlack
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (homeTeamIconRef, homeTeamNameRef, scoreLayoutRef, awayTeamLogoRef, awayTeamNameRef) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.icon_home_team),
                contentDescription = "HomeTeam Logo",
                modifier = Modifier.constrainAs(homeTeamIconRef) {
                    if (isHomeTeamLeft) {
                        end.linkTo(scoreLayoutRef.start, margin = 5.dp)
                    } else {
                        start.linkTo(scoreLayoutRef.end, margin = 5.dp)
                    }
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
                    text = if (isHomeTeamLeft) homeTeamScore.toString() else awayTeamScore.toString(),
                    modifier = Modifier
                        .width(65.dp)
                        .align(Alignment.CenterVertically),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (isHomeTeamLeft) homeTeamScoreColor else awayTeamScoreColor,
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
                    text = if (isHomeTeamLeft) awayTeamScore.toString() else homeTeamScore.toString(),
                    modifier = Modifier
                        .width(65.dp)
                        .align(Alignment.CenterVertically),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (isHomeTeamLeft) awayTeamScoreColor else homeTeamScoreColor,
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
                    if (isHomeTeamLeft) {
                        start.linkTo(scoreLayoutRef.end, margin = 5.dp)
                    } else {
                        end.linkTo(scoreLayoutRef.start, margin = 5.dp)
                    }
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

@ComponentPreview
@Composable
private fun PlayGameScoreBoardPreview() {
    BballTendingTheme {
        PlayGameScoreBoard()
    }
}