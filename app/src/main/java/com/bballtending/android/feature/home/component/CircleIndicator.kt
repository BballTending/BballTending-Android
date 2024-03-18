package com.bballtending.android.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextHintGray

@Composable
fun CircleIndicator(
    modifier: Modifier = Modifier,
    curIdx: Int = 0,
    max: Int = 1
) {
    BballTendingTheme {
        Row(modifier = modifier) {
            for (idx in 0 until max) {
                val color = if (idx == curIdx) Color.White else TextHintGray
                Spacer(
                    modifier = Modifier
                        .padding(start = (1.5).dp, end = (1.5).dp)
                        .size(indicatorSize)
                        .background(color = color, shape = CircleShape)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun CircleIndicatorPreview() {
    BballTendingTheme {
        CircleIndicator()
    }
}

private val indicatorSize: Dp = 5.dp