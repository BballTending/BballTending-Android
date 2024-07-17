package com.bballtending.android.feature.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bballtending.android.R
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack

@Composable
fun PlayerCard(
    name: String,
    number: String,
    position: Position,
    isHomeTeamPlayer: Boolean,
    isHotPlayer: Boolean,
    onPlayerCardClicked: () -> Unit,
    modifier: Modifier = Modifier,
    id: String = ""
) {
    val backIconResId =
        if (isHomeTeamPlayer) R.drawable.icon_home_team else R.drawable.icon_away_team

    BballTendingTheme {
        ConstraintLayout(
            modifier = modifier
                .noRippleClickable { onPlayerCardClicked() }
                .size(70.dp)
                .background(
                    color = BballTendingTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(width = 1.dp, color = TextBlack, shape = RoundedCornerShape(16.dp))
        ) {
            val (numberRef, nameRef, idRef, positionRef, hotPlayerRef, backIconRef) = createRefs()
            Image(
                painter = painterResource(id = backIconResId), contentDescription = "Icon",
                modifier = Modifier
                    .size(40.dp)
                    .constrainAs(backIconRef) {
                        end.linkTo(parent.end, margin = 5.dp)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                    },
                contentScale = ContentScale.Fit,
                alpha = 0.3f
            )
            Text(
                text = number,
                modifier = Modifier.constrainAs(numberRef) {
                    start.linkTo(parent.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 8.dp)
                },
                style = BballTendingTheme.typography.medium.copy(
                    fontSize = 12.sp,
                    color = TextBlack
                ),
                maxLines = 1
            )
            Text(
                text = name,
                modifier = Modifier
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(numberRef.bottom)
                    },
                style = BballTendingTheme.typography.medium.copy(
                    fontSize = 12.sp,
                    color = TextBlack
                ),
                maxLines = 1
            )
            if (id.isNotEmpty()) {
                Text(
                    text = id,
                    modifier = Modifier.constrainAs(idRef) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(nameRef.bottom)
                    },
                    style = BballTendingTheme.typography.medium.copy(
                        fontSize = 7.sp,
                        color = TextBlack
                    ),
                    maxLines = 1
                )
            }
            Text(
                text = position.name,
                modifier = Modifier.constrainAs(positionRef) {
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                style = BballTendingTheme.typography.medium.copy(
                    fontSize = 12.sp,
                    color = TextBlack
                )
            )
            if (isHotPlayer) {
                Image(
                    painter = painterResource(id = R.drawable.icon_hot_player),
                    contentDescription = "Hot Player",
                    modifier = Modifier.constrainAs(hotPlayerRef) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                    }
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun PlayerCardPreview() {
    BballTendingTheme {
        PlayerCard(
            name = "기상호",
            number = "6",
            position = Position.SG,
            isHomeTeamPlayer = true,
            isHotPlayer = true,
            onPlayerCardClicked = {},
            id = "sangho_ki"
        )
    }
}