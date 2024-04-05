package com.bballtending.android.feature.home.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCalendar(
    gameExistDate: ImmutableSet<GameDate>,
    onDateChange: (GameDate) -> Unit,
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now()
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val calendarWidth = screenWidth.minus(40.dp)
    val cellWidth by remember { mutableStateOf(calendarWidth.div(7)) }

    val initPage =
        (currentDate.year - CalendarConfig.yearRange.first) * 12 + currentDate.monthValue - 1
    val pageCount = (CalendarConfig.yearRange.last - CalendarConfig.yearRange.first + 1) * 12

    var selectedDate by remember { mutableStateOf(currentDate) }
    var currentPage by remember { mutableIntStateOf(initPage) }
    val pagerState = rememberPagerState(initialPage = initPage, pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = selectedDate) {
        onDateChange(
            GameDate(
                year = selectedDate.year,
                month = selectedDate.monthValue,
                day = selectedDate.dayOfMonth
            )
        )
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        val delta = (pagerState.currentPage - currentPage).toLong()
        val day = selectedDate.dayOfMonth
        selectedDate = selectedDate.plusMonths(delta).let {
            if (day in 1..it.lengthOfMonth()) it else it.withDayOfMonth(1)
        }
        currentPage = pagerState.currentPage
    }

    BballTendingTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = BballTendingTheme.colors.background)
        ) {
            CalendarTitle(
                selectedDate = selectedDate,
                prevMonthEnable = pagerState.currentPage > 0,
                nextMonthEnable = pagerState.currentPage < pagerState.pageCount - 1,
                onPrevMonth = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(currentPage - 1)
                        }
                    }
                },
                onNextMonth = {
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    }
                })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(Alignment.CenterHorizontally),
                beyondBoundsPageCount = 2
            ) { page ->
                val date = LocalDate.of(
                    CalendarConfig.yearRange.first + page / 12,
                    page % 12 + 1,
                    1
                )

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .fillMaxWidth()
                ) {
                    CalendarHeader(cellWidth = cellWidth)
                    CalendarDay(
                        cellWidth = cellWidth,
                        localDate = date,
                        selectedDate = selectedDate,
                        gameExistDate = gameExistDate,
                        onDayCellClick = { year: Int, month: Int, day: Int ->
                            selectedDate = LocalDate.of(year, month, day)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarTitle(
    selectedDate: LocalDate,
    prevMonthEnable: Boolean,
    nextMonthEnable: Boolean,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = stringResource(
                    id = R.string.calendar_month_format,
                    selectedDate.year.toString(),
                    selectedDate.monthValue.monthToString()
                ),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(Alignment.CenterStart),
                style = BballTendingTheme.typography.black.copy(fontSize = 18.sp)
            )

            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = onPrevMonth,
                    enabled = prevMonthEnable
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_left_arrow_active),
                        contentDescription = "Left Arrow"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                IconButton(
                    onClick = onNextMonth,
                    enabled = nextMonthEnable
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_right_arrow_active),
                        contentDescription = "Right Arrow"
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(cellWidth: Dp) {
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
private fun CalendarDay(
    cellWidth: Dp,
    localDate: LocalDate,
    selectedDate: LocalDate,
    gameExistDate: ImmutableSet<GameDate>,
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
                            isGameRecordExist = false
                        )
                    }
                }
                while (day + firstDayOfWeek <= 7) {
                    val isHover = day == selectedDate.dayOfMonth
                    val isGameRecordExist =
                        gameExistDate.contains(GameDate(localDate.year, localDate.monthValue, day))
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

            while (day <= curMonthSize) {
                Row {
                    for (idx in 0 until 7) {
                        val isNextMonth = day > curMonthSize
                        val isHover = !isNextMonth && day == selectedDate.dayOfMonth
                        val isGameRecordExist = !isNextMonth && gameExistDate.contains(
                            GameDate(
                                localDate.year,
                                localDate.monthValue,
                                day
                            )
                        )

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
                                isGameRecordExist = false
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

private fun Int.monthToString(): String {
    return when (this) {
        in 1 until 10 -> "0$this"
        in 10 until 13 -> this.toString()
        else -> ""
    }
}

private object CalendarConfig {
    private const val START_YEAR: Int = 2000
    val yearRange: IntRange = IntRange(START_YEAR, LocalDate.now().year + 100)
}

private object DayOfWeek {
    private val enums: Array<DayOfWeek> = DayOfWeek.values()

    fun getDisplayName(): List<String> {
        return arrayListOf<String>().apply {
            enums.forEach { enum ->
                val str = when (enum) {
                    DayOfWeek.SUNDAY -> "일"
                    DayOfWeek.MONDAY -> "월"
                    DayOfWeek.TUESDAY -> "화"
                    DayOfWeek.WEDNESDAY -> "수"
                    DayOfWeek.THURSDAY -> "목"
                    DayOfWeek.FRIDAY -> "금"
                    DayOfWeek.SATURDAY -> "토"
                }
                add(str)
            }
        }
    }

    enum class DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
}

@ComponentPreview
@Composable
private fun HorizontalCalendarPreview() {
    BballTendingTheme {
        HorizontalCalendar(
            gameExistDate = setOf<GameDate>().toImmutableSet(),
            onDateChange = {},
            currentDate = LocalDate.of(2024, 7, 5)
        )
    }
}