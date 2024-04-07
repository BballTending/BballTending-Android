package com.bballtending.android.feature.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * 새로운 선수를 추가할 때 사용하는 Modal Dialog
 *
 * @param recentPlayerList  최근 플레이한 선수 리스트
 * @param onAddGamePlayer   선수 추가 Callback
 * @param onDismiss         Dialog Dismiss Callback
 */
@Composable
fun PlayerInfoDialog(
    recentPlayerList: ImmutableList<PlayerData>,
    onAddGamePlayer: (PlayerData) -> Unit,
    onDismiss: () -> Unit = {}
) {
    PlayerInfoDialog(
        title = stringResource(id = R.string.addPlayerInfoDialog_title),
        recentPlayerList = recentPlayerList,
        name = "",
        number = "",
        position = null,
        confirmBtnEnable = false,
        onConfirm = onAddGamePlayer,
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
    recentPlayerList: ImmutableList<PlayerData>,
    originPlayerData: PlayerData,
    onModifyGamePlayer: (PlayerData) -> Unit,
    onDismiss: () -> Unit = {}
) {
    PlayerInfoDialog(
        title = stringResource(id = R.string.modifyPlayerInfoDialog_title),
        recentPlayerList = recentPlayerList,
        name = originPlayerData.name,
        number = originPlayerData.number,
        position = originPlayerData.position,
        confirmBtnEnable = true,
        onConfirm = onModifyGamePlayer,
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
    confirmBtnEnable: Boolean,
    onConfirm: (PlayerData) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

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
                        onClick = onDismiss,
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

                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(26.dp))
                PlayerInfoTextField(
                    inputType = InputType.NAME,
                    value = name,
                    label = stringResource(id = R.string.playerInfoDialog_name),
                    placeholder = stringResource(id = R.string.playerInfoDialog_name_placeholder),
                    onTextChange = {},
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                PlayerInfoTextField(
                    inputType = InputType.NUMBER,
                    value = number,
                    label = stringResource(id = R.string.playerInfoDialog_number),
                    placeholder = stringResource(id = R.string.playerInfoDialog_number_placeholder),
                    onTextChange = {},
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        }
    }
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
            confirmBtnEnable = true,
            onConfirm = {},
            onDismiss = {}
        )
    }
}