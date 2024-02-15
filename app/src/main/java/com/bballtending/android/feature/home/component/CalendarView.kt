package com.bballtending.android.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.domain.calendar.model.CalendarDayData
import com.bballtending.android.domain.calendar.model.CalendarMonthData
import com.bballtending.android.domain.calendar.model.DayOfWeek
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CalendarView(
    calendarMonthData: CalendarMonthData,
    selectedDay: Int,
    onPrevMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onDayCellClick: (CalendarDayData) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarWidth = screenWidth.minus(60.dp)
    val cellWidth = calendarWidth.div(7)

    BballTendingTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(30.dp, 30.dp, 30.dp, 40.dp)
                .background(BballTendingTheme.colors.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        id = R.string.calendar_month_format,
                        calendarMonthData.year,
                        calendarMonthData.month
                    ),
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
                        modifier = Modifier.clickable {
                            onPrevMonth()
                        }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Image(
                        painter = painterResource(id = R.drawable.icon_right_arrow_active),
                        contentDescription = "Right Arrow",
                        modifier = Modifier.clickable {
                            onNextMonth()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Calendar Header
            CalendarHeader(cellWidth = cellWidth)
            // Calendar Day
            calendarMonthData.day2DList.forEachIndexed { index, week ->
                CalendarDayRow(
                    cellWidth = cellWidth,
                    week = week.toImmutableList(),
                    currentMonth = calendarMonthData.month,
                    selectedDay = selectedDay,
                    isLastWeek = index == calendarMonthData.day2DList.size.minus(1),
                    onDayCellClick = onDayCellClick
                )
            }
        }
    }
}

@Composable
fun CalendarHeader(cellWidth: Dp) {
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

@Composable
fun CalendarDayRow(
    cellWidth: Dp,
    week: ImmutableList<CalendarDayData>,
    currentMonth: Int,
    selectedDay: Int,
    isLastWeek: Boolean,
    onDayCellClick: (CalendarDayData) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        week.forEachIndexed { index, calendarDayData ->
            val isOtherMonth = calendarDayData.month != currentMonth
            val isHover = !isOtherMonth && calendarDayData.day == selectedDay
            CalendarDayCell(
                calendarDayData = calendarDayData,
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                isHover = isHover,
                isOtherMonth = isOtherMonth,
                isHorizontalLast = index == week.size.minus(1),
                isVerticalLast = isLastWeek,
                onClick = onDayCellClick
            )
        }
    }
}

@ComponentPreview
@Composable
fun CalendarViewPreview() {
    BballTendingTheme {
        CalendarView(
            calendarMonthData = CalendarMonthData(
                year = 2024,
                month = 2,
                day2DList = listOf(
                    listOf(
                        CalendarDayData(
                            year = 2024,
                            month = 1,
                            day = 28,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 1,
                            day = 29,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 1,
                            day = 30,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 1,
                            day = 31,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 2,
                            day = 1,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 2,
                            day = 2,
                            gameRecord = listOf()
                        ),
                        CalendarDayData(
                            year = 2024,
                            month = 2,
                            day = 3,
                            gameRecord = listOf()
                        )
                    )
                )
            ),
            selectedDay = 3
        )
    }
}