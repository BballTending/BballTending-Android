package com.bballtending.android.feature.playgame.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.bballtending.android.R
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.DevicePreviewLandscape
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import com.bballtending.android.ui.theme.WinScoreRed

@Composable
fun PlayGameBackButtonDialog(
    onResume: () -> Unit,
    onFinish: () -> Unit
) {
    BballTendingTheme {
        Dialog(
            onDismissRequest = onResume,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .wrapContentWidth()
                    .fillMaxHeight(),
                shape = RectangleShape,
                colors = CardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight(),
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .noRippleClickable(onResume)
                            .width(200.dp)
                            .fillMaxHeight()
                            .background(
                                BballTendingTheme.colors.background,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        val (iconRef, titleRef, msgRef, buttonRef) = createRefs()
                        Image(
                            painter = painterResource(id = R.drawable.icon_play_game_dialog_finish),
                            contentDescription = "Confirm",
                            modifier = Modifier
                                .width(60.dp)
                                .wrapContentHeight()
                                .constrainAs(iconRef) {
                                    centerHorizontallyTo(parent)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(titleRef.top)
                                }
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_resumeTitle),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .constrainAs(titleRef) {
                                    centerTo(parent)
                                },
                            style = BballTendingTheme.typography.bold.copy(
                                fontSize = 16.sp,
                                color = WinScoreRed
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_resumeMsg),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .constrainAs(msgRef) {
                                    centerHorizontallyTo(parent)
                                    top.linkTo(titleRef.bottom, margin = 14.dp)
                                },
                            style = BballTendingTheme.typography.regular.copy(
                                fontSize = 12.sp,
                                color = TextHintGray
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_resumeBtn),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = WinScoreRed, shape = RoundedCornerShape(32.dp))
                                .padding(vertical = 9.dp)
                                .constrainAs(buttonRef) {
                                    centerHorizontallyTo(parent)
                                    bottom.linkTo(parent.bottom, margin = 25.dp)
                                },
                            style = BballTendingTheme.typography.medium.copy(
                                fontSize = 12.sp,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    ConstraintLayout(
                        modifier = Modifier
                            .noRippleClickable(onFinish)
                            .width(200.dp)
                            .fillMaxHeight()
                            .background(
                                BballTendingTheme.colors.background,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        val (iconRef, titleRef, msgRef, buttonRef) = createRefs()
                        Image(
                            painter = painterResource(id = R.drawable.icon_play_game_dialog_resume),
                            contentDescription = "Dismiss",
                            modifier = Modifier
                                .width(60.dp)
                                .wrapContentHeight()
                                .constrainAs(iconRef) {
                                    centerHorizontallyTo(parent)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(titleRef.top)
                                }
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_finishTitle),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .constrainAs(titleRef) {
                                    centerTo(parent)
                                },
                            style = BballTendingTheme.typography.bold.copy(
                                fontSize = 16.sp,
                                color = TextBlack
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_finishMsg),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .constrainAs(msgRef) {
                                    centerHorizontallyTo(parent)
                                    top.linkTo(titleRef.bottom, margin = 14.dp)
                                },
                            style = BballTendingTheme.typography.regular.copy(
                                fontSize = 12.sp,
                                color = TextHintGray
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.playGame_dialog_finishBtn),
                            modifier = Modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = TextBlack, shape = RoundedCornerShape(32.dp))
                                .padding(vertical = 9.dp)
                                .constrainAs(buttonRef) {
                                    centerHorizontallyTo(parent)
                                    bottom.linkTo(parent.bottom, margin = 25.dp)
                                },
                            style = BballTendingTheme.typography.medium.copy(
                                fontSize = 12.sp,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@DevicePreviewLandscape
@Composable
private fun PlayGameBackButtonDialogPreview() {
    BballTendingTheme {
        PlayGameBackButtonDialog(
            onResume = {},
            onFinish = {}
        )
    }
}