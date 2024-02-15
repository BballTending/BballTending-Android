package com.bballtending.android.feature.intro

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.bballtending.android.common.util.DLog
import com.bballtending.android.feature.intro.model.IntroUiState
import com.bballtending.android.ui.preview.DevicePreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.TextBlack
import kotlinx.coroutines.delay

const val INTRO_SCREEN_ROUTE: String = "intro"

fun NavGraphBuilder.introScreen(
    onComplete: () -> Unit,
    onLogin: () -> Unit
) {
    composable(
        route = INTRO_SCREEN_ROUTE
    ) {
        IntroScreen(
            onComplete = onComplete,
            onLogin = onLogin
        )
    }
}

@Composable
fun IntroScreen(
    onComplete: () -> Unit,
    onLogin: () -> Unit,
    introViewModel: IntroViewModel = hiltViewModel()
) {
    val uiState: IntroUiState by introViewModel.uiState.collectAsStateWithLifecycle()

    IntroScreen(bottomMsgResId = uiState.bottomMsgResId)

    LaunchedEffect(Unit) {
        introViewModel.responseAutoLogin.collect { responseLogin ->
            DLog.d(INTRO_SCREEN_ROUTE, "responseLogin=$responseLogin")

            delay(2000)
            if (responseLogin != null) {
                onLogin()
            } else {
                onComplete()
            }
        }
    }
}

@Composable
fun IntroScreen(
    bottomMsgResId: Int
) {
    BballTendingTheme {
        val infiniteTransition = rememberInfiniteTransition(label = "BounceAnimation")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -15f,
            animationSpec = infiniteRepeatable(
                animation = tween(400),
                repeatMode = RepeatMode.Reverse
            ),
            label = "BounceAnimation"
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(BballTendingTheme.colors.background)
        ) {
            val (textAppName, imgGoalPost, imgBasketball, textBottomMsg) = createRefs()
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
                    .width(120.dp)
                    .wrapContentHeight()
                    .constrainAs(imgBasketball) {
                        bottom.linkTo(imgGoalPost.top, margin = (-34).dp)
                        centerHorizontallyTo(parent)
                    }
                    .offset(y = offsetY.dp),
                painter = painterResource(id = R.drawable.icon_basketball),
                contentDescription = "Basketball",
                contentScale = ContentScale.Fit
            )
            Image(
                modifier = Modifier
                    .width(150.dp)
                    .wrapContentHeight()
                    .constrainAs(imgGoalPost) {
                        bottom.linkTo(textAppName.top, margin = 20.dp)
                        centerHorizontallyTo(parent)
                    },
                painter = painterResource(id = R.drawable.icon_basketball_goal_post),
                contentDescription = "GoalPost",
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
        IntroScreen(onComplete = {}, onLogin = {})
    }
}