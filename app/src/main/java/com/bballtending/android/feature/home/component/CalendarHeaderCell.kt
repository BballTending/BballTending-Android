package com.bballtending.android.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme

@Composable
fun CalendarHeaderCell(
    text: String,
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Box(
            modifier = modifier
                .background(BballTendingTheme.colors.background)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                style = BballTendingTheme.typography.semiBold.copy(fontSize = 12.sp)
            )
        }
    }
}

@ComponentPreview
@Composable
fun CalendarHeaderCellPreview() {
    BballTendingTheme {
        CalendarHeaderCell(text = "Su")
    }
}