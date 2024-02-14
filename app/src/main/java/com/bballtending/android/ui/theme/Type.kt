package com.bballtending.android.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Stable
class BballTendingTypography(
    appName: TextStyle,
    title: TextStyle,
    bold: TextStyle,
    medium: TextStyle,
    regular: TextStyle
) {
    var appName: TextStyle by mutableStateOf(appName)
        private set
    var title: TextStyle by mutableStateOf(title)
        private set
    var bold: TextStyle by mutableStateOf(bold)
        private set
    var medium: TextStyle by mutableStateOf(medium)
        private set
    var regular: TextStyle by mutableStateOf(regular)
        private set

    fun update(other: BballTendingTypography) {
        appName = other.appName
        title = other.title
        bold = other.bold
        medium = other.medium
        regular = other.regular
    }

    fun copy(
        appTitle: TextStyle = this.appName,
        title: TextStyle = this.title,
        bold: TextStyle = this.bold,
        medium: TextStyle = this.medium,
        regular: TextStyle = this.regular
    ): BballTendingTypography = BballTendingTypography(appTitle, title, bold, medium, regular)
}

@Composable
fun BballTendingTypography(): BballTendingTypography {
    return BballTendingTypography(
        appName = TextStyle(
            fontFamily = SignikaBold,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        ),
        title = TextStyle(
            fontFamily = PretendardBlack,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        ),
        bold = TextStyle(
            fontFamily = PretendardBold,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        ),
        medium = TextStyle(
            fontFamily = PretendardMedium,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.5).sp
        ),
        regular = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.5).sp
        )
    )
}