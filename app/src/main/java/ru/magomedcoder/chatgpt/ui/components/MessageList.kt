package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.utils.Enums

@Composable
fun MessageList(
    list: List<Message>,
    viewModel: ChatViewModel,
    modifier: Modifier
) {
    val scrollListState = rememberLazyListState()
    ConstraintLayout(modifier = modifier) {
        if (list.isEmpty()) {
            EmptyMessage()
        }
        LazyColumn(
            state = scrollListState,
        ) {
            items(list.size) { position ->
                Spacer(modifier = Modifier.height(10.dp))
                val message = list[position]
                when (message.role) {
                    Enums.ASSISTANT.roleName -> LeftView(message)

                    Enums.USER.roleName -> RightView(message)

                    Enums.SYSTEM.roleName -> Text(text = "Ошибка: $message")
                }
                if (position == list.size - 1) {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        if (list.isNotEmpty() && viewModel.isBottom) {
            LaunchedEffect(key1 = list) {
                scrollListState.animateScrollToItem(list.size - 1)
                viewModel.isBottom = false
            }
        }

    }
}
