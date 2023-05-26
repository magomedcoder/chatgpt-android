package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.utils.Enums

@Composable
fun LeftView(message: Message, OnDelete: () -> Unit, OnTextToSpeech: () -> Unit) {
    val content = message.content
    var isDeleteVisible by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .padding(start = 10.dp, end = 120.dp)
            .fillMaxWidth()
    ) {
        val (head, text, delete, retry) = createRefs()
        SelectionContainer(modifier = Modifier
            .padding(start = 7.dp)
            .constrainAs(text) {
                start.linkTo(head.end)
                top.linkTo(head.top)
            }) {
            Card(
                modifier = Modifier
                    .padding(end = 7.dp)
                    .clickable {
                        isDeleteVisible = !isDeleteVisible
                    }, colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF020202)
                )
            ) {
                if (content.isEmpty()) {
                    DotsTyping(
                        modifier = Modifier.padding(15.dp)
                    )
                } else {
                    Text(
                        text = content, color = Color.White, modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }
        if (isDeleteVisible) {
            IconButton(modifier = Modifier
                .constrainAs(delete) {
                    start.linkTo(text.end)
                    top.linkTo(text.top)
                }
                .width(100.dp), onClick = {
                OnDelete.invoke()
            }) {
                Icon(Icons.Filled.Clear, null)
            }
        }
        IconButton(modifier = Modifier
            .constrainAs(retry) {
                start.linkTo(text.end)
                top.linkTo(text.top)
            }
            .width(25.dp), onClick = {
            OnTextToSpeech.invoke()
        }) {
            Icon(Icons.Filled.PlayArrow, null)
        }
    }
}

@Preview
@Composable
fun LeftViewPreview() {
    LeftView(Message(1, Enums.ASSISTANT.roleName, "Test"), {}) {}
}