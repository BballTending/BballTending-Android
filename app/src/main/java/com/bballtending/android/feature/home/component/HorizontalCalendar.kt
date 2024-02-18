package com.bballtending.android.feature.home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.common.util.DLog
import com.bballtending.android.domain.calendar.model.CalendarConfig
import com.bballtending.android.domain.calendar.model.DayOfWeek
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import java.time.LocalDate

private const val TAG: String = "HorizontalCalendar"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCalendar(
    localDate: LocalDate,
    selectedDay: Int,
    gameMap: ImmutableMap<Int, List<GameData>>,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDayCellClick: (year: Int, month: Int, day: Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarWidth = screenWidth.minus(60.dp)
    val cellWidth = calendarWidth.div(7)

    val initPage = (localDate.year - CalendarConfig.yearRange.first) * 12 + localDate.monthValue - 1
    var curMonth by remember { mutableStateOf(localDate) }
    var curPage by remember { mutableStateOf(initPage) }
    val pageCount = (CalendarConfig.yearRange.last - CalendarConfig.yearRange.first + 1) * 12
    val pagerState = rememberPagerState(
        initialPage = initPage,
        pageCount = { pageCount }
    )

    LaunchedEffect(pagerState.currentPage) {
        DLog.d(TAG, "Page changed to ${pagerState.currentPage}")
        val deltaMonth = (pagerState.currentPage - curPage).toLong()
        curMonth = curMonth.plusMonths(deltaMonth)
        curPage = pagerState.currentPage
    }

    BballTendingTheme {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(30.dp, 30.dp, 30.dp, 40.dp)
                .background(BballTendingTheme.colors.background)
        ) {
            // Calendar Title
            CalendarTitle(
                year = curMonth.year,
                month = curMonth.monthValue,
                onPrevMonth = onPrevMonth,
                onNextMonth = onNextMonth
            )
            Spacer(modifier = Modifier.height(20.dp))


            HorizontalPager(
                state = pagerState
            ) { page ->
                val date =
                    LocalDate.of(CalendarConfig.yearRange.first + page / 12, page % 12 + 1, 1)
                Column {
                    // Calendar Header
                    CalendarHeader(cellWidth = cellWidth)
                    // Calendar Day
                    CalendarDay(
                        cellWidth = cellWidth,
                        localDate = date,
                        selectedDay = selectedDay,
                        gameMap = gameMap.toImmutableMap(),
                        onDayCellClick = onDayCellClick
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarTitle(
    year: Int,
    month: Int,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    BballTendingTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.calendar_month_format, year, month),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterStart),
                style = BballTendingTheme.typography.black.copy(fontSize = 18.sp)
            )

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_left_arrow_active),
                    contentDescription = "Left Arrow",
                    modifier = Modifier
                        .noRippleClickable {
                            onPrevMonth()
                        }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(
                    painter = painterResource(id = R.drawable.icon_right_arrow_active),
                    contentDescription = "Right Arrow",
                    modifier = Modifier
                        .noRippleClickable {
                            onNextMonth()
                        }
                )
            }
        }
    }
}


@Composable
fun CalendarHeader(cellWidth: Dp) {
    BballTendingTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) {
            val dayOfWeek = DayOfWeek.getDisplayName()

            dayOfWeek.forEach { day ->
                CalendarHeaderCell(
                    text = day,
                    modifier = Modifier
                        .width(cellWidth)
                        .height(cellWidth),
                )
            }
        }
    }
}

@Composable
fun CalendarDay(
    cellWidth: Dp,
    localDate: LocalDate,
    selectedDay: Int,
    gameMap: ImmutableMap<Int, List<GameData>>,
    onDayCellClick: (year: Int, month: Int, day: Int) -> Unit
) {
    val firstDayOfWeek = localDate.dayOfWeek.value.mod(7)
    val curMonthSize = localDate.lengthOfMonth()

    BballTendingTheme {
        Column {
            var day = 1
            Row {
                // 이번 달의 처음 날짜가 시작하는 요일까지는 이전 달로 채움
                if (firstDayOfWeek > 0) {
                    val prevMonthLocalDate = localDate.minusMonths(1)
                    val prevYear = prevMonthLocalDate.year
                    val prevMonth = prevMonthLocalDate.monthValue
                    val prevMonthSize = prevMonthLocalDate.lengthOfMonth()

                    for (idx in firstDayOfWeek - 1 downTo 0 step 1) {
                        CalendarDayCell(
                            year = prevYear,
                            month = prevMonth,
                            day = prevMonthSize - idx,
                            modifier = Modifier
                                .width(cellWidth)
                                .height(cellWidth),
                            isHover = false,
                            isOtherMonth = true,
                            isGameRecordExist = false,
                            onClick = onDayCellClick
                        )
                    }
                }
                while (day + firstDayOfWeek <= 7) {
                    val isHover = day == selectedDay
                    val isGameRecordExist = gameMap[day]?.isNotEmpty() ?: false
                    CalendarDayCell(
                        year = localDate.year,
                        month = localDate.monthValue,
                        day = day,
                        modifier = Modifier
                            .width(cellWidth)
                            .height(cellWidth),
                        isHover = isHover,
                        isOtherMonth = false,
                        isGameRecordExist = isGameRecordExist,
                        onClick = onDayCellClick
                    )
                    day += 1
                }
            }

            while (day < curMonthSize) {
                Row {
                    for (idx in 0 until 7) {
                        val isNextMonth = day > curMonthSize
                        val isHover = !isNextMonth && day == selectedDay
                        val isGameRecordExist = !isNextMonth && gameMap[day]?.isNotEmpty() ?: false

                        if (isNextMonth) {
                            val nextMonthLocalDate = localDate.plusMonths(1)
                            val nextYear = nextMonthLocalDate.year
                            val nextMonth = nextMonthLocalDate.monthValue
                            CalendarDayCell(
                                year = nextYear,
                                month = nextMonth,
                                day = day.mod(curMonthSize),
                                modifier = Modifier
                                    .width(cellWidth)
                                    .height(cellWidth),
                                isHover = false,
                                isOtherMonth = true,
                                isGameRecordExist = false,
                                onClick = onDayCellClick
                            )
                        } else {
                            CalendarDayCell(
                                year = localDate.year,
                                month = localDate.monthValue,
                                day = day,
                                modifier = Modifier
                                    .width(cellWidth)
                                    .height(cellWidth),
                                isHover = isHover,
                                isOtherMonth = false,
                                isGameRecordExist = isGameRecordExist,
                                onClick = onDayCellClick
                            )
                        }
                        day += 1
                    }
                }
            }
        }
    }
}