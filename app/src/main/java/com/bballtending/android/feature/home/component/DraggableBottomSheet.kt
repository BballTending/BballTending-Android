package com.bballtending.android.feature.home.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bballtending.android.common.util.DLog
import com.bballtending.android.feature.shadow
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.ShadowBlack
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val TAG: String = "DraggableBottomSheet"
private const val ANIM_DURATION: Int = 500

@Composable
fun DraggableBottomSheet(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = remember { configuration.screenHeightDp.dp }

    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    var initY by remember { mutableFloatStateOf(0f) }

    BballTendingTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeight(screenHeight)
                .offset() {
                    IntOffset(x = 0, y = offsetY.value.roundToInt())
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = { offset: Offset ->
                            DLog.e(TAG, "offset=$offset")
                        },
                        onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                            val nextY = initY + offsetY.value + dragAmount
                            DLog.d(
                                TAG,
                                "initY=$initY, nextY=$nextY, offsetY=${offsetY.value}, dragAmount=$dragAmount"
                            )
                            coroutineScope.launch {
                                offsetY.snapTo(
                                    (offsetY.value + dragAmount).coerceIn(
                                        maximumValue = 0f,
                                        minimumValue = -initY
                                    )
                                )
                            }
                        },
                        onDragEnd = {
                            val absOffsetY = offsetY.value.absoluteValue
                            DLog.d(TAG, "onDragEnd, absOffsetY=$absOffsetY")
                            val threshold = initY.div(2)
                            if (absOffsetY > threshold) {
                                coroutineScope.launch {
                                    offsetY.animateTo(
                                        targetValue = -initY,
                                        animationSpec = tween(
                                            durationMillis = ANIM_DURATION,
                                            delayMillis = 0
                                        )
                                    )
                                }
                            } else {
                                coroutineScope.launch {
                                    offsetY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(
                                            durationMillis = ANIM_DURATION,
                                            delayMillis = 0
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
                .shadow(
                    color = ShadowBlack,
                    offsetY = (-1).dp,
                    blurRadius = 5.dp,
                    topLeftRound = 16.dp,
                    topRightRound = 16.dp
                )
                .background(
                    color = BballTendingTheme.colors.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .onGloballyPositioned {
                    if (initY == 0f) {
                        initY = it.positionInParent().y
                        DLog.d(TAG, "initY changed to $initY")
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(35.dp)
                    .height(5.dp)
                    .background(color = BorderGray, shape = RoundedCornerShape(10.dp))
            )
        }
    }
}

@ComponentPreview
@Composable
fun DraggableBottomSheetPreview() {
    BballTendingTheme {
        DraggableBottomSheet(
            modifier = Modifier.height(400.dp)
        )
    }
}