package com.bballtending.android.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.domain.calendar.model.CalendarDayData
import com.bballtending.android.feature.Border
import com.bballtending.android.feature.border
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BackCalendarActiveBlue
import com.bballtending.android.ui.theme.BackCalendarHoverBlue
import com.bballtending.android.ui.theme.BackCalendarInactiveGray
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextCalendarInactiveGray

@Composable
fun CalendarDayCell(
    calendarDayData: CalendarDayData,
    modifier: Modifier = Modifier,
    isHover: Boolean = false,
    isOtherMonth: Boolean = false,
    isHorizontalLast: Boolean = false,
    isVerticalLast: Boolean = false,
    onClick: (CalendarDayData) -> Unit = {}
) {
    BballTendingTheme {
        val backgroundColor = when {
            isHover -> BackCalendarHoverBlue
            !calendarDayData.isEmpty -> BackCalendarActiveBlue
            isOtherMonth -> BackCalendarInactiveGray
            else -> BballTendingTheme.colors.background
        }
        val textColor = when {
            isHover -> Color.White
            isOtherMonth -> TextCalendarInactiveGray
            else -> TextBlack
        }
        val borderList = arrayListOf(Border.LEFT, Border.TOP).apply {
            if (isHorizontalLast) add(Border.RIGHT)
            if (isVerticalLast) add(Border.BOTTOM)
        }

        Box(
            modifier = modifier
                .background(backgroundColor)
                .clickable {
                    onClick(calendarDayData)
                }
                .border(
                    1.dp,
                    BorderGray,
                    borderList = borderList
                )
        ) {
            Text(
                text = calendarDayData.day.toString(),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 12.sp,
                    color = textColor
                )
            )
        }
    }
}

@ComponentPreview
@Composable
fun CalendarDayCellPreview() {
    BballTendingTheme {
        CalendarDayCell(
            calendarDayData = CalendarDayData(
                year = 2024,
                month = 2,
                day = 15,
                gameRecord = listOf()
            )
        )
    }
}