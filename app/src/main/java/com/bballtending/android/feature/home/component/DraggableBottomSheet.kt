package com.bballtending.android.feature.home.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bballtending.android.common.util.DLog
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val TAG: String = "DraggableBottomSheet"
private const val ANIM_DURATION: Int = 500

@Composable
fun DraggableBottomSheet(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = remember { configuration.screenHeightDp.dp }
    val density = LocalDensity.current
    val marginTop = remember { with(density) { 6.dp.toPx() } }

    val coroutineScope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    var initY by remember { mutableFloatStateOf(0f) }

    BballTendingTheme {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeight(screenHeight)
                .offset {
                    IntOffset(x = 0, y = offsetY.value.roundToInt())
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = { offset: Offset ->
                            DLog.d(TAG, "offset=$offset")
                        },
                        onVerticalDrag = { _: PointerInputChange, dragAmount: Float ->
                            coroutineScope.launch {
                                offsetY.snapTo(
                                    (offsetY.value + dragAmount).coerceIn(
                                        maximumValue = 0f,
                                        minimumValue = -initY + marginTop
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
                                        targetValue = -initY + marginTop,
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
                    elevation = 5.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(top = 2.dp)
                .background(
                    color = BballTendingTheme.colors.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .onGloballyPositioned {
                    if (initY == 0f) {
                        initY = it.positionInParent().y
                        DLog.d(TAG, "initY changed to $initY")
                    }
                }
        ) {
            content()
        }
    }
}

@ComponentPreview
@Composable
fun DraggableBottomSheetPreview() {
    BballTendingTheme {
        DraggableBottomSheet(
            modifier = Modifier.height(400.dp)
        ) {
        }
    }
}