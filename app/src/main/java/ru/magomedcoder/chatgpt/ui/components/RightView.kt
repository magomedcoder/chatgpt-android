package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
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
fun RightView(message: Message, onDelete: () -> Unit, onRetry: () -> Unit) {
    val content = message.content
    var isOperateVisible by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(end = 5.dp, start = 60.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(end = 5.dp, start = 60.dp)
        ) {
            val (head, text, delete, retry) = createRefs()
            SelectionContainer(modifier = Modifier.constrainAs(text) {
                top.linkTo(head.top)
                end.linkTo(head.start)
            }) {
                Card(
                    modifier = Modifier.padding(start = 7.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF020202)
                    )
                ) {
                    Box(modifier = Modifier.clickable {
                        isOperateVisible = !isOperateVisible
                    }) {
                        Text(
                            text = content,
                            color = Color.White,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
            if (isOperateVisible) {
                IconButton(modifier = Modifier
                    .constrainAs(delete) {
                        top.linkTo(text.top)
                        end.linkTo(text.start)
                    }
                    .width(25.dp), onClick = {
                    onDelete.invoke()
                }) {
                    Icon(Icons.Filled.Clear, null)
                }
                IconButton(modifier = Modifier
                    .constrainAs(retry) {
                        top.linkTo(delete.top)
                        end.linkTo(delete.start)
                    }
                    .width(25.dp), onClick = {
                    isOperateVisible = false
                    onRetry.invoke()
                }) {
                    Icon(Icons.Filled.Refresh, null)
                }
            }
        }
    }
}

@Preview
@Composable
fun RightViewPreview() {
    LeftView(Message(1, Enums.USER.roleName, "Test")) {}
}