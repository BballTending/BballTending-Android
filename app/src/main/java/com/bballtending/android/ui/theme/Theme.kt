package com.bballtending.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class BballTendingColors(
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    background: Color,
    onBackground: Color,
    surface: Color,
    onSurface60: Color,
    onSurface50: Color,
    onSurface40: Color,
    onSurface30: Color,
    onSurface20: Color,
    onSurface10: Color,
    surfaceContainer: Color,
    onSurfaceContainer: Color,
    surfaceContainerHigh: Color,
    onSurfaceContainerHigh: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var surface by mutableStateOf(surface)
        private set
    var onSurface60 by mutableStateOf(onSurface60)
        private set
    var onSurface50 by mutableStateOf(onSurface50)
        private set
    var onSurface40 by mutableStateOf(onSurface40)
        private set
    var onSurface30 by mutableStateOf(onSurface30)
        private set
    var onSurface20 by mutableStateOf(onSurface20)
        private set
    var onSurface10 by mutableStateOf(onSurface10)
        private set
    var surfaceContainer by mutableStateOf(surfaceContainer)
        private set
    var onSurfaceContainer by mutableStateOf(onSurfaceContainer)
        private set
    var surfaceContainerHigh by mutableStateOf(surfaceContainerHigh)
        private set
    var onSurfaceContainerHigh by mutableStateOf(onSurfaceContainerHigh)
        private set

    var isLight by mutableStateOf(isLight)

    fun copy(): BballTendingColors = BballTendingColors(
        primary,
        onPrimary,
        secondary,
        background,
        onBackground,
        surface,
        onSurface60,
        onSurface50,
        onSurface40,
        onSurface30,
        onSurface20,
        onSurface10,
        surfaceContainer,
        onSurfaceContainer,
        surfaceContainerHigh,
        onSurfaceContainerHigh,
        isLight
    )

    fun update(other: BballTendingColors) {
        primary = other.primary
        onPrimary = other.onPrimary
        secondary = other.secondary
        background = other.background
        onBackground = other.onBackground
        surface = other.surface
        onSurface60 = other.onSurface60
        onSurface50 = other.onSurface50
        onSurface40 = other.onSurface40
        onSurface30 = other.onSurface30
        onSurface20 = other.onSurface20
        onSurface10 = other.onSurface10
        surfaceContainer = other.surfaceContainer
        onSurfaceContainer = other.onSurfaceContainer
        surfaceContainerHigh = other.surfaceContainerHigh
        onSurfaceContainerHigh = other.onSurfaceContainerHigh
        isLight = other.isLight
    }
}

fun bballTendingColors(
    primary: Color = Primary,
    onPrimary: Color = OnPrimary,
    secondary: Color = Secondary,
    background: Color = Background,
    onBackground: Color = OnBackground,
    surface: Color = Background,
    onSurface60: Color = OnBackground,
    onSurface50: Color = OnBackground,
    onSurface40: Color = OnBackground,
    onSurface30: Color = OnBackground,
    onSurface20: Color = OnBackground,
    onSurface10: Color = OnBackground,
    surfaceContainer: Color = OnBackground,
    onSurfaceContainer: Color = OnBackground,
    surfaceContainerHigh: Color = OnBackground,
    onSurfaceContainerHigh: Color = OnBackground,
    isLight: Boolean = true
): BballTendingColors {
    return BballTendingColors(
        primary,
        onPrimary,
        secondary,
        background,
        onBackground,
        surface,
        onSurface60,
        onSurface50,
        onSurface40,
        onSurface30,
        onSurface20,
        onSurface10,
        surfaceContainer,
        onSurfaceContainer,
        surfaceContainerHigh,
        onSurfaceContainerHigh,
        isLight
    )
}

private val LocalBballTendingColors = staticCompositionLocalOf<BballTendingColors> {
    error("No BballTendingColors provided")
}
private val LocalBballTendingTypography = staticCompositionLocalOf<BballTendingTypography> {
    error("No BballTendingTypography provided")
}

object BballTendingTheme {
    val colors: BballTendingColors @Composable get() = LocalBballTendingColors.current
    val typography: BballTendingTypography @Composable get() = LocalBballTendingTypography.current
}

@Composable
fun ProvideBballTendingColorAndTypography(
    colors: BballTendingColors,
    typography: BballTendingTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)
    CompositionLocalProvider(
        LocalBballTendingColors provides provideColors,
        LocalBballTendingTypography provides provideTypography,
        content = content
    )
}

@Composable
fun BballTendingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = bballTendingColors()
    val typography = BballTendingTypography()
    ProvideBballTendingColorAndTypography(colors, typography) {
        MaterialTheme(content = content)
    }
}