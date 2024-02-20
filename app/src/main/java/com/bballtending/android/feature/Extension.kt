package com.bballtending.android.feature

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bballtending.android.ui.theme.ShadowBlack

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

fun Modifier.shadow(
    color: Color = ShadowBlack,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    topLeftRound: Dp = 0.dp,
    topRightRound: Dp = 0.dp,
    bottomLeftRound: Dp = 0.dp,
    bottomRightRound: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            val topLeftPixel = topLeftRound.toPx()
            val topRightPixel = topRightRound.toPx()
            val bottomLeftPixel = bottomLeftRound.toPx()
            val bottomRightPixel = bottomRightRound.toPx()
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(
                            offset = Offset(leftPixel, topPixel),
                            size = Size(rightPixel - leftPixel, bottomPixel - topPixel)
                        ),
                        topLeft = CornerRadius(topLeftPixel, topLeftPixel),
                        topRight = CornerRadius(topRightPixel, topRightPixel),
                        bottomLeft = CornerRadius(bottomLeftPixel, bottomLeftPixel),
                        bottomRight = CornerRadius(bottomRightPixel, bottomRightPixel)
                    )
                )
            }
            canvas.drawPath(path = path, paint = paint)
        }
    }
)