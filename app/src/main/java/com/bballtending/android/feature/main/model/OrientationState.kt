package com.bballtending.android.feature.main.model

sealed class OrientationState {
    object PORTRAIT : OrientationState()
    
    object LANDSCAPE : OrientationState()
}