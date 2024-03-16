package com.bballtending.android.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.TestModule
import com.bballtending.android.domain.game.model.SortType
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.feature.Border
import com.bballtending.android.feature.border
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.text.DecimalFormat

@Composable
fun ScoreTable(
    playerDataList: ImmutableList<PlayerData>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var sortType by remember { mutableStateOf(SortType.DEFAULT) }
    val originalPlayerDataList by remember { mutableStateOf(playerDataList) }
    var sortedPlayerDataList by remember { mutableStateOf(playerDataList) }

    BballTendingTheme {
        Column(
            modifier = modifier
                .wrapContentSize()
                .horizontalScroll(scrollState)
        ) {
            ScoreTableHeaderRow(
                selectedSortType = sortType,
                onSortTypeChange = { selectedSortType ->
                    sortType = if (sortType == selectedSortType) {
                        sortedPlayerDataList = originalPlayerDataList
                        SortType.DEFAULT
                    } else {
                        sortedPlayerDataList =
                            sortPlayerDataList(originalPlayerDataList, selectedSortType)
                        selectedSortType
                    }
                }
            )

            sortedPlayerDataList.forEachIndexed { idx, playerData ->
                ScoreTableCellRow(
                    playerData = playerData,
                    selectedSortType = sortType,
                    isLastRow = idx == sortedPlayerDataList.lastIndex
                )
            }
        }
    }
}

private fun sortPlayerDataList(
    list: ImmutableList<PlayerData>,
    sortType: SortType
): ImmutableList<PlayerData> {
    val mutableList = list.toMutableList()
    return when (sortType) {
        SortType.DEFAULT -> list
        SortType.SCORE -> mutableList.sortedByDescending { it.score }
        SortType.REBOUND -> mutableList.sortedByDescending { it.rebound }
        SortType.ASSIST -> mutableList.sortedByDescending { it.assist }
        SortType.STEAL -> mutableList.sortedByDescending { it.steal }
        SortType.BLOCK -> mutableList.sortedByDescending { it.block }
        SortType.FIELD_GOAL_RATIO -> mutableList.sortedByDescending { it.fieldGoalSuccess }
        SortType.FIELD_GOAL_PERCENTAGE -> mutableList.sortedByDescending { it.fieldGoalPercentage }
        SortType.THREE_POINT_RATIO -> mutableList.sortedByDescending { it.threePointSuccess }
        SortType.THREE_POINT_PERCENTAGE -> mutableList.sortedByDescending { it.threePointPercentage }
        SortType.FOUL -> mutableList.sortedByDescending { it.foul }
    }.toImmutableList()
}

@Composable
private fun ScoreTableHeaderRow(
    selectedSortType: SortType,
    onSortTypeChange: (sortType: SortType) -> Unit
) {
    BballTendingTheme {
        Row(modifier = Modifier.wrapContentSize()) {
            // 선수명
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = playerNameWidth)
                    .heightIn(min = commonHeight),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_playerName),
                    modifier = Modifier.padding(start = 10.dp, top = 9.dp, bottom = 7.dp),
                    style = BballTendingTheme.typography.bold.copy(
                        color = TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 득점
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = scoreWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.SCORE) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_score),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.SCORE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.SCORE) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 리바운드
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = reboundWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.REBOUND) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_rebound),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.REBOUND) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.REBOUND) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 어시스트
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = assistWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.ASSIST) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_assist),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.ASSIST) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.ASSIST) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 스틸
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = stealWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.STEAL) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_steal),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.STEAL) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.STEAL) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 블록슛
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = blockWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.BLOCK) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_block),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.BLOCK) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.BLOCK) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 야투
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = fieldGoalRatioWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.FIELD_GOAL_RATIO) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_fieldGoal_ratio),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.FIELD_GOAL_RATIO) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.FIELD_GOAL_RATIO) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 야투%
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = fieldGoalPercentageWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.FIELD_GOAL_PERCENTAGE) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_fieldGoal_percentage),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.FIELD_GOAL_PERCENTAGE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.FIELD_GOAL_PERCENTAGE) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 3점슛
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = threePointRatioWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.THREE_POINT_RATIO) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_threePoint_ratio),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.THREE_POINT_RATIO) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.THREE_POINT_RATIO) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 3점슛%
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP)
                    )
                    .widthIn(min = threePointPercentageWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.THREE_POINT_PERCENTAGE) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_threePoint_percentage),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.THREE_POINT_PERCENTAGE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.THREE_POINT_PERCENTAGE) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            // 파울
            Row(
                modifier = Modifier
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = listOf(Border.LEFT, Border.TOP, Border.RIGHT)
                    )
                    .widthIn(min = foulWidth)
                    .heightIn(min = commonHeight)
                    .noRippleClickable { onSortTypeChange(SortType.FOUL) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.scoreTable_foul),
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        end = 5.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.bold.copy(
                        color = if (selectedSortType == SortType.FOUL) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
                Image(
                    painter = if (selectedSortType == SortType.FOUL) {
                        painterResource(id = R.drawable.icon_arrow_down_selected)
                    } else {
                        painterResource(id = R.drawable.icon_arrow_down)
                    },
                    contentDescription = "정렬 버튼",
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun ScoreTableCellRow(
    playerData: PlayerData,
    selectedSortType: SortType,
    isLastRow: Boolean = false
) {
    BballTendingTheme {
        Row {
            // 선수명
            Row(
                modifier = Modifier
                    .width(playerNameWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = playerData.number,
                    modifier = Modifier.padding(
                        start = 10.dp,
                        top = 9.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.medium.copy(
                        color = TextBlack,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = playerData.name,
                    modifier = Modifier.padding(
                        start = 5.dp,
                        top = 9.dp,
                        end = 10.dp,
                        bottom = 7.dp
                    ),
                    style = BballTendingTheme.typography.medium.copy(
                        color = TextBlack,
                        fontSize = 12.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            // 득점
            Box(
                modifier = Modifier
                    .widthIn(min = scoreWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.score.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.SCORE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 리바운드
            Box(
                modifier = Modifier
                    .widthIn(min = reboundWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.rebound.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.REBOUND) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 어시스트
            Box(
                modifier = Modifier
                    .widthIn(min = assistWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.assist.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.ASSIST) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 스틸
            Box(
                modifier = Modifier
                    .widthIn(min = stealWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.steal.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.STEAL) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 블록슛
            Box(
                modifier = Modifier
                    .widthIn(min = blockWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.block.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.BLOCK) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 야투
            Box(
                modifier = Modifier
                    .widthIn(min = fieldGoalRatioWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.fieldGoalRatio,
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.FIELD_GOAL_RATIO) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 야투%
            Box(
                modifier = Modifier
                    .widthIn(min = fieldGoalPercentageWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = formatter.format(playerData.fieldGoalPercentage),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.FIELD_GOAL_PERCENTAGE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 3점슛
            Box(
                modifier = Modifier
                    .widthIn(min = threePointRatioWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = playerData.threePointRatio,
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.THREE_POINT_RATIO) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 3점슛%
            Box(
                modifier = Modifier
                    .widthIn(min = threePointPercentageWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.BOTTOM)
                        } else {
                            listOf(Border.LEFT, Border.TOP)
                        }
                    )
            ) {
                Text(
                    text = formatter.format(playerData.threePointPercentage),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.THREE_POINT_PERCENTAGE) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
            // 파울
            Box(
                modifier = Modifier
                    .widthIn(min = foulWidth)
                    .heightIn(min = commonHeight)
                    .border(
                        strokeWidth = 1.dp,
                        color = BorderGray,
                        borderList = if (isLastRow) {
                            listOf(Border.LEFT, Border.TOP, Border.RIGHT)
                        } else {
                            listOf(Border.LEFT, Border.TOP, Border.RIGHT, Border.BOTTOM)
                        }
                    )
            ) {
                Text(
                    text = playerData.foul.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = BballTendingTheme.typography.medium.copy(
                        color = if (selectedSortType == SortType.FOUL) BballTendingTheme.colors.primary else TextBlack,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
@ComponentPreview
fun ScoreTablePreview() {
    BballTendingTheme {
        val testData = TestModule.createTestData()
        ScoreTable(testData.homeTeamPlayer.toImmutableList())
    }
}

private val formatter = DecimalFormat("##.#")
private val commonHeight: Dp = 36.dp
private val playerNameWidth: Dp = 120.dp
private val scoreWidth: Dp = 56.dp
private val reboundWidth: Dp = 78.dp
private val assistWidth: Dp = 78.dp
private val stealWidth: Dp = 56.dp
private val blockWidth: Dp = 67.dp
private val fieldGoalRatioWidth: Dp = 56.dp
private val fieldGoalPercentageWidth: Dp = 67.dp
private val threePointRatioWidth: Dp = 64.dp
private val threePointPercentageWidth: Dp = 75.dp
private val foulWidth: Dp = 56.dp