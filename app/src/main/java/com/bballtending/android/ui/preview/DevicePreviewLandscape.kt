package com.bballtending.android.ui.preview

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Galaxy S23 Ultra",
    showBackground = true,
    device = "spec:width=360dp,height=772dp,dpi=411,orientation=landscape"
)
@Preview(
    name = "Galaxy S23",
    showBackground = true,
    device = "spec:width=360dp,height=800dp,dpi=500,orientation=landscape"
)
annotation class DevicePreviewLandscape
