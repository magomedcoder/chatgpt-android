package ru.magomedcoder.chatgpt.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.magomedcoder.chatgpt.ui.theme.Purple80

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClick: () -> Unit,
) {
    val textEmpty: Boolean by derivedStateOf { value.text.isEmpty() }
    Surface(
        modifier = modifier,
        color = Purple80
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            CompositionLocalProvider() {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFFFFFFFF)),
                        decorationBox = { innerTextField ->
                            if (textEmpty) {
                                Text(
                                    text = "Написать сообщение",
                                    color = Color(0xFF939393),
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
                if (!textEmpty) {
                    ChatButton(
                        onClick = { onClick.invoke() },
                        modifier = Modifier.then(Modifier.size(44.dp)),
                        indication = rememberRipple(bounded = false, radius = 44.dp / 2)
                    ) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatInputPreview() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    ChatInput(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.padding(5.dp),
        onClick = {
            text = TextFieldValue("")
        }
    )
}