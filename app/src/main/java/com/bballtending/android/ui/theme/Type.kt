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
    black: TextStyle,
    bold: TextStyle,
    semiBold: TextStyle,
    medium: TextStyle,
    regular: TextStyle
) {
    var appName: TextStyle by mutableStateOf(appName)
        private set
    var black: TextStyle by mutableStateOf(black)
        private set
    var bold: TextStyle by mutableStateOf(bold)
        private set
    var semiBold: TextStyle by mutableStateOf(semiBold)
        private set
    var medium: TextStyle by mutableStateOf(medium)
        private set
    var regular: TextStyle by mutableStateOf(regular)
        private set

    fun update(other: BballTendingTypography) {
        appName = other.appName
        black = other.black
        bold = other.bold
        semiBold = other.semiBold
        medium = other.medium
        regular = other.regular
    }

    fun copy(
        appTitle: TextStyle = this.appName,
        black: TextStyle = this.black,
        bold: TextStyle = this.bold,
        semiBold: TextStyle = this.semiBold,
        medium: TextStyle = this.medium,
        regular: TextStyle = this.regular
    ): BballTendingTypography =
        BballTendingTypography(appTitle, black, bold, semiBold, medium, regular)
}

@Composable
fun BballTendingTypography(): BballTendingTypography {
    return BballTendingTypography(
        appName = TextStyle(
            fontFamily = SignikaBold,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        ),
        black = TextStyle(
            fontFamily = PretendardBlack,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        ),
        bold = TextStyle(
            fontFamily = PretendardBold,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        ),
        semiBold = TextStyle(
            fontFamily = PretendardSemibold,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        ),
        medium = TextStyle(
            fontFamily = PretendardMedium,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        ),
        regular = TextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            letterSpacing = (-0.5).sp,
            color = TextBlack
        )
    )
}