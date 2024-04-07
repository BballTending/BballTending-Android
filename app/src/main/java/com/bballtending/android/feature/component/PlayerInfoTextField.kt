package com.bballtending.android.feature.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bballtending.android.R
import com.bballtending.android.ui.noRippleClickable
import com.bballtending.android.ui.preview.ComponentPreview
import com.bballtending.android.ui.theme.BballTendingTheme
import com.bballtending.android.ui.theme.BorderGray
import com.bballtending.android.ui.theme.CancelRed
import com.bballtending.android.ui.theme.TextHintGray

/**
 * 선수명 및 등번호 입력 화면에서 사용하는 CustomTextField
 *
 * @see com.bballtending.android.feature.dialog.PlayerInfoDialog
 */
@Composable
fun PlayerInfoTextField(
    inputType: InputType,
    value: String,
    label: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value) }
    var errorMsgVisible by remember { mutableStateOf(false) }
    val inputRegex by remember { mutableStateOf(getInputRegex(inputType)) }

    LaunchedEffect(key1 = text) {
        onTextChange(text)
    }

    BballTendingTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = BballTendingTheme.colors.background)
        ) {
            Text(
                text = label,
                modifier = Modifier.align(Alignment.Start),
                style = BballTendingTheme.typography.regular.copy(fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            BasicTextField(
                value = text,
                onValueChange = {
                    if (inputRegex.matches(it)) {
                        text = it
                        errorMsgVisible = false
                    } else {
                        errorMsgVisible = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = BballTendingTheme.typography.regular.copy(fontSize = 15.sp),
                singleLine = true
            ) { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (errorMsgVisible) CancelRed else BorderGray,
                            shape = RectangleShape
                        )
                        .padding(start = 15.dp, top = 15.dp, bottom = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            modifier = Modifier.align(Alignment.CenterStart),
                            style = BballTendingTheme.typography.regular.copy(
                                fontSize = 15.sp,
                                color = TextHintGray
                            )
                        )
                    }
                    if (text.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_close_round_duotone),
                            contentDescription = "Clear",
                            modifier = Modifier
                                .noRippleClickable { text = "" }
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))

            val errorMsg = if (errorMsgVisible) {
                when (inputType) {
                    InputType.NAME -> stringResource(id = R.string.errorMsg_textField_playerName)
                    InputType.NUMBER -> stringResource(id = R.string.errorMsg_textField_playerNumber)
                }
            } else {
                ""
            }
            Text(
                text = errorMsg,
                style = BballTendingTheme.typography.regular.copy(
                    fontSize = 14.sp,
                    color = CancelRed
                )
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PlayerInfoTextFieldPreview() {
    BballTendingTheme {
        PlayerInfoTextField(
            inputType = InputType.NAME,
            value = "기상호",
            label = "선수명",
            placeholder = "선수명을 입력해 주세요.",
            onTextChange = {}
        )
    }
}

private fun getInputRegex(inputType: InputType): Regex = when (inputType) {
    InputType.NAME -> {
        Regex("[ㄱ-ㅎ가-힣a-zA-Z]+")
    }

    InputType.NUMBER -> {
        Regex("[0-9]+")
    }
}

enum class InputType {
    /**
     * 선수명
     */
    NAME,

    /**
     * 등번호
     */
    NUMBER
}