package com.bballtending.android.feature.addgame.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bballtending.android.R
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray

@Composable
fun AddPlayerCard(
    onAddPlayerCardClick: () -> Unit
) {
    BballTendingTheme {
        Box(
            modifier = Modifier
                .noRippleClickable { onAddPlayerCardClick() }
                .size(100.dp)
                .background(color = BballTendingTheme.colors.background)
                .border(width = 1.dp, color = BorderGray, shape = RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_add_player),
                contentDescription = "Add Player",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@ComponentPreview
@Composable
private fun AddPlayerCardPreview() {
    BballTendingTheme {
        AddPlayerCard(onAddPlayerCardClick = {})
    }
}