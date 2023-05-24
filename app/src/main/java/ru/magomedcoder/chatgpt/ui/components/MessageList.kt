package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.utils.enums.Role

@Composable
fun MessageList(list: List<Message> = emptyList()) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(list.size) { position ->
            Spacer(modifier = Modifier.height(10.dp))
            val message = list[position]
            when (message.role) {
                Role.ASSISTANT.roleName -> {
                    LeftView(message.content)
                }

                Role.USER.roleName -> {
                    RightView(message.content)
                }

                Role.SYSTEAM.roleName -> {
                    Box(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SelectionContainer {
                            Text(
                                text = message.content,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            if (position == list.size - 1) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}