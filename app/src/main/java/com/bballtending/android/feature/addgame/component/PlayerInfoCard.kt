package com.bballtending.android.feature.addgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.TestModule
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack

@Composable
fun PlayerInfoCard(
    isHomeTeam: Boolean,
    playerData: PlayerData,
    onPlayerInfoCardClick: (PlayerData) -> Unit
) {
    val bgIconResId = if (isHomeTeam) R.drawable.icon_home_team else R.drawable.icon_away_team
    BballTendingTheme {
        Box(
            modifier = Modifier
                .noRippleClickable {
                    onPlayerInfoCardClick(playerData)
                }
                .size(100.dp)
                .background(color = BballTendingTheme.colors.background)
                .border(width = 1.dp, color = TextBlack, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = bgIconResId), contentDescription = "BG Icon",
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 5.dp)
                    .size(60.dp)
                    .align(Alignment.BottomEnd),
                contentScale = ContentScale.Fit,
                alpha = 0.3f
            )
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 12.dp, top = 15.dp, end = 12.dp)
            ) {
                Text(
                    text = playerData.number,
                    style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp)
                )
                Text(
                    text = playerData.name,
                    style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp)
                )
            }
            Text(
                text = playerData.position.name,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
                style = BballTendingTheme.typography.medium.copy(fontSize = 15.sp)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerInfoCardPreview() {
    BballTendingTheme {
        PlayerInfoCard(
            isHomeTeam = true,
            playerData = TestModule.createTestData().homeTeamPlayer.first(),
            onPlayerInfoCardClick = {})
    }
}