package ru.magomedcoder.chatgpt.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.blankj.utilcode.util.ScreenUtils
import ru.magomedcoder.chatgpt.domain.model.Message
import ru.magomedcoder.chatgpt.domain.model.VolumeState
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.utils.Enums

@Composable
fun MessageList(
    context: Context,
    list: List<Message>,
    viewModel: ChatViewModel,
    volumeState: VolumeState,
    modifier: Modifier,
    onShowEditKeyClick: () -> Unit
) {
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    val scrollListState = rememberLazyListState()
    val scHeight = ScreenUtils.getScreenHeight() / 3 * 2
    if (volumeState.touchUp) {
        LaunchedEffect(key1 = System.currentTimeMillis()) {
            scrollListState.animateScrollBy((-scHeight).toFloat())
        }
    }
    if (volumeState.touchDown) {
        LaunchedEffect(key1 = System.currentTimeMillis()) {
            scrollListState.animateScrollBy(scHeight.toFloat())
        }
    }
    ConstraintLayout(modifier = modifier) {
        if (list.isEmpty()) {
            EmptyMessage()
        }
        LazyColumn(state = scrollListState) {
            items(list.size) { position ->
                Spacer(modifier = Modifier.height(10.dp))
                val message = list[position]
                when (message.role) {
                    Enums.ASSISTANT.roleName -> LeftView(
                        message,
                        {
                            viewModel.alreadyDeleteMessage = message
                            isShowDeleteDialog = true
                        },
                        {
                            viewModel.onTextFieldValueChange(message.content)
                            viewModel.textToSpeech(context)
                        }
                    )

                    Enums.USER.roleName -> RightView(message, {
                        viewModel.alreadyDeleteMessage = message
                        isShowDeleteDialog = true
                    }, {
                        if (position != -1) {
                            viewModel.retryMessage(position)
                        }
                    })

                    Enums.SYSTEM.roleName -> Box(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .clickable {
                                    onShowEditKeyClick.invoke()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Lock, null, Modifier.padding(end = 8.dp))
                            Text(text = "Ошибка токена")
                        }
                    }
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
    if (isShowDeleteDialog) {
        AlertDialog(
            onDismissRequest = { isShowDeleteDialog = false },
            title = { Text(text = "Вы действительно хотите удалить?", fontSize = 15.sp) },
            confirmButton = {
                Button(onClick = {
                    viewModel.alreadyDeleteMessage?.let { viewModel.deleteMessage(it) }
                }) {
                    Text(text = "Удалить")
                }
            },
            dismissButton = {
                Button(onClick = {
                    isShowDeleteDialog = false
                }) {
                    Text(text = "Отмена")
                }
            }
        )
    }
}
