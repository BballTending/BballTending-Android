package com.bballtending.android.feature.dialog

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import com.bballtending.android.feature.addgame.component.PlayerInfoCard
import com.bballtending.android.feature.component.InputType
import com.bballtending.android.feature.component.PlayerInfoTextField
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextHintGray
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

/**
 * 새로운 선수를 추가할 때 사용하는 Modal Dialog
 *
 * @param recentPlayerList  최근 플레이한 선수 리스트
 * @param onAddGamePlayer   선수 추가 Callback
 * @param onDismiss         Dialog Dismiss Callback
 */
@Composable
fun PlayerInfoDialog(
    isHomeTeamPlayer: Boolean,
    recentPlayerList: ImmutableList<PlayerData>,
    onAddGamePlayer: (Boolean, String, String, Position) -> Boolean,
    onDismiss: () -> Unit = {}
) {
    val teamName = if (isHomeTeamPlayer) "홈 팀" else "어웨이 팀"
    PlayerInfoDialog(
        title = stringResource(id = R.string.addPlayerInfoDialog_title, teamName),
        recentPlayerList = recentPlayerList,
        name = "",
        number = "",
        position = null,
        confirmBtnText = stringResource(id = R.string.msg_add_player),
        confirmBtnEnable = false,
        onConfirm = { name, number, position ->
            onAddGamePlayer(isHomeTeamPlayer, name, number, position)
        },
        onDismiss = onDismiss
    )
}

/**
 * 기존 선수 정보를 수정할 때 사용하는 Modal Dialog
 *
 *  @param recentPlayerList     최근 플레이한 선수 리스트
 *  @param originPlayerData     수정할 선수 데이터
 *  @param onModifyGamePlayer   선수 추가 Callback
 *  @param onDismiss            Dialog Dismiss Callback
 */
@Composable
fun PlayerInfoDialog(
    isHomeTeamPlayer: Boolean,
    recentPlayerList: ImmutableList<PlayerData>,
    originPlayerData: PlayerData,
    onModifyGamePlayer: (Boolean, PlayerData) -> Boolean,
    onDismiss: () -> Unit = {}
) {
    val teamName = if (isHomeTeamPlayer) "홈 팀" else "어웨이 팀"
    PlayerInfoDialog(
        title = stringResource(id = R.string.modifyPlayerInfoDialog_title, teamName),
        recentPlayerList = recentPlayerList,
        name = originPlayerData.name,
        number = originPlayerData.number,
        position = originPlayerData.position,
        confirmBtnText = stringResource(id = R.string.msg_modify_player),
        confirmBtnEnable = true,
        onConfirm = { name, number, position ->
            onModifyGamePlayer(
                isHomeTeamPlayer,
                originPlayerData.copy(
                    name = name,
                    number = number,
                    position = position
                )
            )
        },
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerInfoDialog(
    title: String,
    recentPlayerList: ImmutableList<PlayerData>,
    name: String,
    number: String,
    position: Position?,
    confirmBtnText: String,
    confirmBtnEnable: Boolean,
    onConfirm: (String, String, Position) -> Boolean,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var localName by remember { mutableStateOf(name) }
    var localNumber by remember { mutableStateOf(number) }
    var localPosition by remember { mutableStateOf(position) }
    var localConfirmBtnEnable by remember { mutableStateOf(confirmBtnEnable) }

    LaunchedEffect(key1 = localName, key2 = localNumber, key3 = localPosition) {
        localConfirmBtnEnable =
            localName.isNotEmpty() && localNumber.isNotEmpty() && localPosition != null
    }

    val context = LocalContext.current

    BballTendingTheme {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = BballTendingTheme.colors.background,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss()
                                }
                            }
                        },
                        modifier = Modifier.padding(start = 3.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_back),
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = title,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.medium.copy(fontSize = 18.sp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (recentPlayerList.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.playerInfoDialog_recentPlayer),
                        modifier = Modifier.padding(start = 15.dp),
                        style = BballTendingTheme.typography.bold.copy(fontSize = 16.sp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (playerData in recentPlayerList) {
                            item {
                                PlayerInfoCard(
                                    isHomeTeam = true,
                                    playerData = playerData,
                                    onPlayerInfoCardClick = {
                                        localName = it.name
                                        localNumber = it.number
                                        localPosition = it.position
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                }
                PlayerInfoTextField(
                    inputType = InputType.NAME,
                    value = localName,
                    label = stringResource(id = R.string.playerInfoDialog_name),
                    placeholder = stringResource(id = R.string.playerInfoDialog_name_placeholder),
                    onTextChange = {
                        localName = it
                    },
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerInfoTextField(
                    inputType = InputType.NUMBER,
                    value = localNumber,
                    label = stringResource(id = R.string.playerInfoDialog_number),
                    placeholder = stringResource(id = R.string.playerInfoDialog_number_placeholder),
                    onTextChange = {
                        localNumber = it
                    },
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.playerInfoDialog_position),
                    modifier = Modifier.padding(start = 15.dp),
                    style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
                )
                // Position - PG
                PositionItem(
                    position = Position.PG,
                    isSelect = localPosition == Position.PG,
                    onPositionItemClick = {
                        localPosition = it
                    }
                )
                // Position - SG
                PositionItem(
                    position = Position.SG,
                    isSelect = localPosition == Position.SG,
                    onPositionItemClick = {
                        localPosition = it
                    }
                )
                // Position - SF
                PositionItem(
                    position = Position.SF,
                    isSelect = localPosition == Position.SF,
                    onPositionItemClick = {
                        localPosition = it
                    }
                )
                // Position - PF
                PositionItem(
                    position = Position.PF,
                    isSelect = localPosition == Position.PF,
                    onPositionItemClick = {
                        localPosition = it
                    }
                )
                // Position - C
                PositionItem(
                    position = Position.C,
                    isSelect = localPosition == Position.C,
                    onPositionItemClick = {
                        localPosition = it
                    }
                )
                Spacer(modifier = Modifier.height(19.dp))
                Button(
                    onClick = {
                        if (onConfirm(localName.trim(), localNumber.trim(), localPosition!!)) {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                onDismiss()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getText(R.string.errorMsg_playerInfo_duplicate),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    enabled = localConfirmBtnEnable,
                    shape = RectangleShape,
                    colors = ButtonColors(
                        containerColor = BballTendingTheme.colors.primary,
                        contentColor = BballTendingTheme.colors.primary,
                        disabledContainerColor = BorderGray,
                        disabledContentColor = BorderGray
                    ),
                    contentPadding = PaddingValues(top = 18.dp, bottom = 17.dp)
                ) {
                    Text(
                        text = confirmBtnText,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = BballTendingTheme.typography.bold.copy(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PositionItem(
    position: Position,
    isSelect: Boolean,
    onPositionItemClick: (Position) -> Unit
) {
    val selectedBgColor = TextBlack
    val unselectedBgColor = BballTendingTheme.colors.background
    val selectedFontColor = Color.White
    val unselectedFontColor = TextHintGray

    BballTendingTheme {
        Row(
            modifier = Modifier
                .noRippleClickable {
                    onPositionItemClick(position)
                }
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .fillMaxWidth()
                .background(color = if (isSelect) selectedBgColor else unselectedBgColor)
                .then(
                    if (isSelect)
                        Modifier
                    else
                        Modifier.border(width = 1.dp, color = BorderGray)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = position.name,
                modifier = Modifier
                    .padding(start = 15.dp, top = 13.dp, bottom = 13.dp)
                    .widthIn(min = 20.dp),
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 15.sp,
                    color = if (isSelect) selectedFontColor else unselectedFontColor
                )
            )
            Text(
                text = stringResource(id = getPositionDescResId(position)),
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 13.dp,
                    end = 15.dp,
                    bottom = 13.dp
                ),
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 15.sp,
                    color = if (isSelect) selectedFontColor else unselectedFontColor
                )
            )
        }
    }
}

@StringRes
private fun getPositionDescResId(position: Position): Int = when (position) {
    Position.PG -> R.string.positionDesc_PG
    Position.SG -> R.string.positionDesc_SG
    Position.SF -> R.string.positionDesc_SF
    Position.PF -> R.string.positionDesc_PF
    Position.C -> R.string.positionDesc_C
}

@ComponentPreview
@Composable
private fun PlayerInfoDialogPreview() {
    BballTendingTheme {
        PlayerInfoDialog(
            title = "선수 정보 입력하기",
            recentPlayerList = listOf<PlayerData>().toImmutableList(),
            name = "기상호",
            number = "6",
            position = Position.SG,
            confirmBtnText = "우하하",
            confirmBtnEnable = true,
            onConfirm = { _, _, _ -> true },
            onDismiss = {}
        )
    }
}