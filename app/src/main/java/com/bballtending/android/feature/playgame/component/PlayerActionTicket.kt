package com.bballtending.android.feature.playgame.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.domain.player.model.PlayerAction
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BackActionTicketAway
import com.bballtending.android.ui.theme.BackActionTicketHome
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.CancelRed
import com.bballtending.android.ui.theme.Primary
import com.bballtending.android.ui.theme.TextBlack

@Composable
fun PlayerActionTicket(
    isHomeTeam: Boolean,
    action: PlayerAction,
    onPlayerActionTicketClicked: (action: PlayerAction, isHomeTeam: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isLeftSide: Boolean = true
) {
    val strResId = when (action) {
        PlayerAction.TWO_POINT_SUCCESS -> R.string.playerAction_twoPointSuccess
        PlayerAction.TWO_POINT_FAIL -> R.string.playerAction_twoPointFail
        PlayerAction.THREE_POINT_SUCCESS -> R.string.playerAction_threePointSuccess
        PlayerAction.THREE_POINT_FAIL -> R.string.playerAction_threePointFail
        PlayerAction.REBOUND -> R.string.playerAction_reboud
        PlayerAction.STEAL -> R.string.playerAction_steal
        PlayerAction.ASSIST -> R.string.playerAction_assist
        PlayerAction.BLOCK -> R.string.playerAction_block
        PlayerAction.FOUL -> R.string.playerAction_foul
        PlayerAction.TURNOVER -> R.string.playerAction_turnover
    }
    val textColor = when (action) {
        PlayerAction.TWO_POINT_SUCCESS,
        PlayerAction.THREE_POINT_SUCCESS,
        PlayerAction.REBOUND,
        PlayerAction.STEAL,
        PlayerAction.ASSIST,
        PlayerAction.BLOCK -> Primary

        PlayerAction.TWO_POINT_FAIL,
        PlayerAction.THREE_POINT_FAIL -> TextBlack

        PlayerAction.FOUL,
        PlayerAction.TURNOVER -> CancelRed
    }
    BballTendingTheme {
        Box(
            modifier = modifier
                .noRippleClickable { onPlayerActionTicketClicked(action, isHomeTeam) }
                .size(120.dp, 35.dp)
                .background(
                    color = BballTendingTheme.colors.background,
                    shape = if (isLeftSide) RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    else RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
                .drawBehind {
                    val width = size.width
                    val height = size.height
                    val cornerRadius = 12.dp.toPx()
                    val strokeWidthPx = 1.dp.toPx()
                    val borderColor = BorderGray

                    val coloredWidth = 13.dp.toPx()
                    val coloredHeight = 35.dp.toPx()
                    if (isLeftSide) {
                        drawRect(
                            color = if (isHomeTeam) BackActionTicketHome else BackActionTicketAway,
                            topLeft = Offset(0f, 0f),
                            size = Size(coloredWidth, coloredHeight)
                        )
                        // Top right arc
                        drawArc(
                            color = borderColor,
                            startAngle = 270f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset(x = width - cornerRadius * 2, y = 0f),
                            size = Size(cornerRadius * 2, cornerRadius * 2),
                            style = Stroke(width = strokeWidthPx)
                        )
                        // Bottom right arc
                        drawArc(
                            color = borderColor,
                            startAngle = 0f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset(
                                x = width - 2 * cornerRadius,
                                y = height - 2 * cornerRadius
                            ),
                            size = Size(cornerRadius * 2, cornerRadius * 2),
                            style = Stroke(width = strokeWidthPx)
                        )
                        // Right line
                        drawLine(
                            color = borderColor,
                            start = Offset(x = width, y = cornerRadius),
                            end = Offset(x = width, y = height - cornerRadius),
                            strokeWidth = strokeWidthPx
                        )
                    } else {
                        drawRect(
                            color = if (isHomeTeam) BackActionTicketHome else BackActionTicketAway,
                            topLeft = Offset(width - coloredWidth, 0f),
                            size = Size(coloredWidth, coloredHeight)
                        )
                        // Top left arc
                        drawArc(
                            color = borderColor,
                            startAngle = 180f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset.Zero,
                            size = Size(cornerRadius * 2, cornerRadius * 2),
                            style = Stroke(width = strokeWidthPx)
                        )
                        // Bottom left arc
                        drawArc(
                            color = borderColor,
                            startAngle = 90f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset(x = 0f, y = height - 2 * cornerRadius),
                            size = Size(cornerRadius * 2, cornerRadius * 2),
                            style = Stroke(width = strokeWidthPx)
                        )
                        // Left line
                        drawLine(
                            color = borderColor,
                            start = Offset(x = 0f, y = cornerRadius),
                            end = Offset(x = 0f, y = height - cornerRadius),
                            strokeWidth = strokeWidthPx
                        )
                    }
                    // Top line
                    drawLine(
                        color = borderColor,
                        start = Offset(coloredWidth, 0f),
                        end = Offset(width - cornerRadius, 0f),
                        strokeWidth = strokeWidthPx
                    )
                    // Bottom line
                    drawLine(
                        color = borderColor,
                        start = Offset(x = coloredWidth, y = height),
                        end = Offset(x = width - cornerRadius, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }
        ) {
            Text(
                text = stringResource(id = strResId),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                style = BballTendingTheme.typography.semiBold.copy(
                    fontSize = 12.sp,
                    color = textColor
                ),
                textAlign = if (isLeftSide) TextAlign.Left else TextAlign.Right
            )
        }
    }
}

@ComponentPreview
@Composable
fun PlayerActionListPreview() {
    BballTendingTheme {
        PlayerActionTicket(
            isHomeTeam = true,
            action = PlayerAction.ASSIST,
            onPlayerActionTicketClicked = { _, _ -> },
            isLeftSide = false
        )
    }
}