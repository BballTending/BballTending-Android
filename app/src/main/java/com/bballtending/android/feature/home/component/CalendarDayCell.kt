package com.bballtending.android.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BackCalendarActiveBlue
import com.bballtending.android.ui.theme.BackCalendarHoverBlue
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import com.bballtending.android.ui.theme.TextCalendarInactiveGray

@Composable
fun CalendarDayCell(
    year: Int,
    month: Int,
    day: Int,
    modifier: Modifier = Modifier,
    isHover: Boolean = false,
    isGameRecordExist: Boolean = false,
    isOtherMonth: Boolean = false,
    onClick: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> }
) {
    BballTendingTheme {
        val backgroundColor = when {
            isHover -> BackCalendarHoverBlue
            isGameRecordExist -> BackCalendarActiveBlue
            else -> BballTendingTheme.colors.background
        }
        val textColor = when {
            isHover -> Color.White
            isOtherMonth -> TextCalendarInactiveGray
            else -> TextBlack
        }
        val textStyle =
            if (isHover) BballTendingTheme.typography.medium else BballTendingTheme.typography.regular

        Box(
            modifier = modifier
                .fillMaxSize()
                .noRippleClickable {
                    onClick(year, month, day)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .background(backgroundColor, RoundedCornerShape(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isOtherMonth) "" else day.toString(),
                    modifier = Modifier
                        .wrapContentSize(),
                    textAlign = TextAlign.Center,
                    style = textStyle.copy(
                        fontSize = 12.sp,
                        color = textColor
                    )
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun CalendarDayCellPreview() {
    BballTendingTheme {
        CalendarDayCell(
            year = 2024,
            month = 2,
            day = 15,
            isGameRecordExist = false
        )
    }
}