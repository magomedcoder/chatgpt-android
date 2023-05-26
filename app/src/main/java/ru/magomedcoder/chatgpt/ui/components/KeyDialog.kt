package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.ui.theme.Purple40
import ru.magomedcoder.chatgpt.ui.theme.Purple80

@Composable
fun KeyDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    var key by remember { mutableStateOf(Constants.GlobalConfig.apiKey) }
    AlertDialog(
        title = {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (cleaR) = createRefs()
                Text(
                    text = "OpenAI API",
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Column(modifier = Modifier
                    .width(120.dp)
                    .padding(top = 10.dp)
                    .constrainAs(cleaR) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                uriHandler.openUri("https://platform.openai.com/account/api-keys")
                            },
                        text = "Создать токен",
                        color = Color(0xFFFFFFFF),
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        },
        containerColor = Purple80,
        onDismissRequest = {
            onDismiss.invoke()
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val shape = RoundedCornerShape(10)
                OutlinedTextField(
                    value = key,
                    onValueChange = {
                        key = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Purple40,
                        cursorColor = Color.White
                    ),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple40, shape),
                    shape = shape,
                    placeholder = {
                        Text("Введите ключ")
                    },
                    maxLines = 1,
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onConfirm.invoke(key)
                }
            ) {
                Text(text = "Сохранить")
            }
        }
    )
}


@Preview
@Composable
fun KeyDialogPreview() {
    KeyDialog({}, {})
}