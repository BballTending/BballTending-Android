package com.bballtending.android.feature.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bballtending.android.BballTendingApp
import com.bballtending.android.feature.main.model.OrientationState
import com.bballtending.android.ui.theme.BballTendingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            model.splashScreenCondition.value
        }

        setContent {
            BballTendingTheme {
                BballTendingApp(
                    onFinish = { finish() },
                    requestPortraitMode = model::requestPortraitMode,
                    requestLandscapeMode = model::requestLandscapeMode
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.orientationState.collect { orientationState ->
                    when (orientationState) {
                        OrientationState.PORTRAIT -> {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                        }

                        OrientationState.LANDSCAPE -> {
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
                        }
                    }
                }
            }
        }
    }
}