package ru.magomedcoder.chatgpt.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.ui.theme.Purple80

@SuppressLint("UnrememberedMutableState")
@Composable
fun ImageInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClick: () -> Unit
) {
    val textEmpty: Boolean by derivedStateOf { value.text.isEmpty() }
    Row(verticalAlignment = Alignment.Bottom) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple80)
        ) {
            val (cleaR) = createRefs()
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 15.dp, bottom = 50.dp, end = 15.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                ),
                cursorBrush = SolidColor(Color(0xFFFFFFFF)),
                decorationBox = { innerTextField ->
                    if (textEmpty) {
                        Text(
                            text = "Текстовое описание желаемых изображений. Максимальная длина - 1000 символов.",
                            color = Color(0xFF939393),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
            Row(modifier = Modifier
                .padding(bottom = 5.dp, end = 10.dp)
                .constrainAs(cleaR) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
                if (!textEmpty) {
                    Button(
                        onClick = {
                            onClick.invoke()
                        },
                        modifier = Modifier,
                        shape = RoundedCornerShape(50.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Сгенерировать",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageInputPreview() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    ImageInput(
        value = text,
        onValueChange = { text = it },
        onClick = {
            text = TextFieldValue("")
        },
    )
}