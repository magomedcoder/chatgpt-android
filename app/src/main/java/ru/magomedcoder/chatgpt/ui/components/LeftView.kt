package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.utils.Enums

@Composable
fun LeftView(message: Message) {
    val content = message.content
    ConstraintLayout(
        modifier = Modifier
            .padding(start = 10.dp, end = 120.dp)
            .fillMaxWidth()
    ) {
        val (head, text) = createRefs()
        SelectionContainer(
            modifier = Modifier
                .padding(start = 7.dp)
                .constrainAs(text) {
                    start.linkTo(head.end)
                    top.linkTo(head.top)
                }
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF020202)
                )
            ) {
                if (content.isEmpty()) {
                    DotsTyping(
                        modifier = Modifier.padding(15.dp)
                    )
                } else {
                    Text(
                        text = content,
                        color = Color.White,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LeftViewPreview() {
    LeftView(Message(1, Enums.ASSISTANT.roleName, "Test"))
}