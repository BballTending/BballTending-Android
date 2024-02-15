package com.bballtending.android.feature

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

enum class Border {
    LEFT, TOP, RIGHT, BOTTOM
}

fun Modifier.border(
    strokeWidth: Dp,
    color: Color,
    borderList: List<Border>
) = composed {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    drawBehind {
        val width = size.width
        val height = size.height

        borderList.forEach { border ->
            when (border) {
                Border.LEFT -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }

                Border.TOP -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = width, y = 0f),
                        strokeWidth = strokeWidthPx
                    )
                }

                Border.RIGHT -> {
                    drawLine(
                        color = color,
                        start = Offset(x = width, y = 0f),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }

                Border.BOTTOM -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = height),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidthPx
                    )
                }
            }
        }
    }
}