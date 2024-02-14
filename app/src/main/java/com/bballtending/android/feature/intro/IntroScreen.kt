package com.bballtending.android.feature.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bballtending.android.R
import com.bballtending.android.feature.intro.model.IntroUiState
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack

const val INTRO_SCREEN_ROUTE: String = "intro"

fun NavGraphBuilder.introScreen(
    onComplete: () -> Unit,
    onLogin: () -> Unit
) {
    composable(
        route = INTRO_SCREEN_ROUTE
    ) {
        IntroScreen(onComplete = onComplete)
    }
}

@Composable
fun IntroScreen(
    onComplete: () -> Unit,
    introViewModel: IntroViewModel = hiltViewModel()
) {
    val uiState: IntroUiState by introViewModel.uiState.collectAsStateWithLifecycle()

    IntroScreen(
        onComplete = onComplete,
        bottomMsgResId = uiState.bottomMsgResId
    )
}

@Composable
fun IntroScreen(
    onComplete: () -> Unit,
    bottomMsgResId: Int
) {
    BballTendingTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background)
        ) {
            val (textAppName, imgAppLogo, textBottomMsg) = createRefs()
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(textAppName) {
                        centerTo(parent)
                    },
                text = buildAnnotatedString {
                    withStyle(style = BballTendingTheme.typography.appName.toSpanStyle()) {
                        append("B")
                    }
                    withStyle(
                        style = BballTendingTheme.typography.appName.copy(fontSize = 32.sp)
                            .toSpanStyle()
                    ) {
                        append("BALL")
                    }
                    withStyle(style = BballTendingTheme.typography.appName.toSpanStyle()) {
                        append("T")
                    }
                    withStyle(
                        style = BballTendingTheme.typography.appName.copy(fontSize = 32.sp)
                            .toSpanStyle()
                    ) {
                        append("ENDING")
                    }
                },
                style = BballTendingTheme.typography.appName,
                color = TextBlack,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .wrapContentHeight()
                    .constrainAs(imgAppLogo) {
                        bottom.linkTo(textAppName.top, margin = 20.dp)
                        centerHorizontallyTo(parent)
                    },
                painter = painterResource(id = R.drawable.icon_intro_app_logo),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit
            )
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(textBottomMsg) {
                        bottom.linkTo(parent.bottom, margin = 115.dp)
                        centerHorizontallyTo(parent)
                    },
                text = stringResource(id = bottomMsgResId),
                style = BballTendingTheme.typography.medium.copy(fontSize = 14.sp),
                color = TextBlack,
                textAlign = TextAlign.Center
            )
        }
    }
}


@DevicePreview
@Composable
fun IntroScreenPreview() {
    BballTendingTheme {
        IntroScreen(onComplete = {})
    }
}