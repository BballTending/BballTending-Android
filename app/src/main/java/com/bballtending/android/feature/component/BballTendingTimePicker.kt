package com.bballtending.android.feature.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.bballtending.android.R
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlin.math.absoluteValue

@Composable
fun BballTendingTimePicker(
    initHour: Int,
    initMinute: Int,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BballTendingTheme {
        Row(modifier = modifier) {
            VerticalNumberPicker(
                initNumber = initHour,
                minNumber = 0,
                maxNumber = 24,
                onNumberChanged = onHourChanged
            )

            Image(
                painter = painterResource(id = R.drawable.icon_time_divider),
                contentDescription = ":",
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            VerticalNumberPicker(
                initNumber = initMinute,
                minNumber = 0,
                maxNumber = 60,
                onNumberChanged = onMinuteChanged
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VerticalNumberPicker(
    initNumber: Int,
    minNumber: Int,
    maxNumber: Int,
    onNumberChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = initNumber, pageCount = { maxNumber })

    LaunchedEffect(key1 = pagerState.settledPage) {
        val curPage = pagerState.settledPage
        onNumberChanged(curPage + minNumber)
    }

    BballTendingTheme {
        VerticalPager(
            state = pagerState,
            modifier = modifier
                .widthIn(min = 130.dp)
                .height(170.dp)
                .wrapContentHeight(),
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = object : PagerSnapDistance {
                    override fun calculateTargetPage(
                        startPage: Int,
                        suggestedTargetPage: Int,
                        velocity: Float,
                        pageSize: Int,
                        pageSpacing: Int
                    ): Int {
                        return suggestedTargetPage
                    }
                }
            ),
            contentPadding = PaddingValues(vertical = 70.dp),
            pageSpacing = 25.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset =
                            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                        // We animate the alpha, between 18% and 100%
                        alpha = lerp(
                            start = 0.18f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        lerp(
                            start = 0.8f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                    }
            ) {
                Text(
                    text = (minNumber + page).toString(),
                    modifier = Modifier
                        .padding(horizontal = 50.dp)
                        .align(Alignment.Center),
                    style = BballTendingTheme.typography.bold.copy(fontSize = 26.sp)
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun BballTendingTimePickerPreview() {
    BballTendingTheme {
        BballTendingTimePicker(
            initHour = 14,
            initMinute = 28,
            onHourChanged = {},
            onMinuteChanged = {}
        )
    }
}