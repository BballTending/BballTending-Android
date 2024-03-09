package com.bballtending.android.feature.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.feature.Border
import com.bballtending.android.feature.border
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.CancelRed
import com.bballtending.android.ui.theme.TextBlack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTypeDialog(
    onDismissRequest: () -> Unit = {},
    onGameTypeSelect: (gameType: GameType) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    BballTendingTheme {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        disabledContainerColor = BorderGray
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onGameTypeSelect(GameType.FULL_COURT)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .border(
                                    strokeWidth = 1.dp,
                                    color = BorderGray,
                                    borderList = listOf(Border.BOTTOM)
                                )
                        ) {
                            Text(
                                text = stringResource(id = R.string.gameTypeDialog_fullCourt),
                                modifier = Modifier.wrapContentSize(),
                                style = BballTendingTheme.typography.medium.copy(
                                    color = TextBlack,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onGameTypeSelect(GameType.HALF_COURT)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.gameTypeDialog_halfCourt),
                            modifier = Modifier.wrapContentSize(),
                            style = BballTendingTheme.typography.medium.copy(
                                color = TextBlack,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 7.dp, end = 10.dp, bottom = 20.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        disabledContainerColor = BorderGray
                    )
                ) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = R.string.gameTypeDialog_cancel),
                            modifier = Modifier.wrapContentSize(),
                            style = BballTendingTheme.typography.medium.copy(
                                color = CancelRed,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
fun GameTypeDialogPreview() {
    BballTendingTheme {
        GameTypeDialog()
    }
}