package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.domain.model.Dialog
import ru.magomedcoder.chatgpt.ui.theme.Purple40
import ru.magomedcoder.chatgpt.ui.theme.Purple80

@Composable
fun SideBar(
    list: List<Dialog>,
    currentDialog: Dialog,
    onButtonClick: () -> Unit,
    onItemClick: (Dialog) -> Unit,
    onDismiss: () -> Unit,
    onScreen: () -> Unit,
) {
    var isShowEditKey by remember { mutableStateOf(false) }
    var isAboutDialog by remember { mutableStateOf(false) }
    if (Constants.GlobalConfig.apiKey.isEmpty()) {
        isShowEditKey = true
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxHeight()
            .background(Purple80)
    ) {
        val (topR, bottomR) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(topR) {
                    top.linkTo(parent.top)
                    bottom.linkTo(bottomR.top)
                }
                .fillMaxHeight()
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = MutableInteractionSource()
                )
                .padding(start = 16.dp, top = 50.dp, bottom = 16.dp, end = 16.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (cleaR) = createRefs()
                Row() {
                    Button(
                        onClick = { onButtonClick.invoke() }
                    ) {
                        Text(text = "Создать диалог")
                    }
                    Button(
                        modifier = Modifier.padding(start = 16.dp),
                        onClick = { onScreen.invoke() }
                    ) {
                        Text(text = "Изображение")
                    }
                }
                Column(modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .constrainAs(cleaR) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    IconButton(
                        onClick = {
                            onDismiss.invoke()
                        }) {
                        Icon(Icons.Filled.Clear, null)
                    }
                }
            }
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (cleaR) = createRefs()
                Text(
                    color = Color.White,
                    text = "Диалоги",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                Column(modifier = Modifier
                    .width(100.dp)
                    .height(30.dp)
                    .constrainAs(cleaR) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                isShowEditKey = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Lock, null, Modifier.padding(end = 8.dp))
                        Text(text = "Ключ")
                    }
                }
            }
            val scrollState = rememberLazyListState()
            LazyColumn(state = scrollState) {
                items(list.size) { position ->
                    val dialog = list[position]
                    val isMe = dialog.id == currentDialog.id
                    Card(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            onItemClick.invoke(dialog)
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Purple40
                        )
                    ) {
                        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                            val (textR, cleaR) = createRefs()
                            val title = dialog.title.ifEmpty {
                                "Диалог №" + (dialog.id)
                            }
                            Text(
                                text = title,
                                maxLines = 1,
                                modifier = Modifier
                                    .constrainAs(textR) {
                                        start.linkTo(parent.start)
                                        end.linkTo(cleaR.start)
                                        top.linkTo(parent.top)
                                    }
                                    .padding(
                                        start = 30.dp,
                                        top = 10.dp,
                                        bottom = 10.dp,
                                        end = 10.dp
                                    )
                                    .fillMaxWidth(),
                                color = if (isMe) {
                                    Color.White
                                } else {
                                    Color.Gray
                                },
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        Box(modifier = Modifier
            .constrainAs(bottomR) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                color = Color.White,
                text = "О приложении",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .clickable {
                        isAboutDialog = true
                    },

                )
        }
    }
    if (isShowEditKey) {
        KeyDialog(
            onConfirm = {
                Constants.GlobalConfig.apiKey = it
                isShowEditKey = false
            },
            onDismiss = {
                isShowEditKey = false
            }
        )
    }
    if (isAboutDialog) {
        AboutDialog(onDismiss = { isAboutDialog = false })
    }
}

@Composable
@Preview
fun SideBarPreview() {
    val currentDialog = Dialog(id = 1, title = "Test", lastDialogTime = System.currentTimeMillis())
    SideBar(mutableListOf<Dialog>().apply {
        add(Dialog(title = "Test 1", lastDialogTime = System.currentTimeMillis()))
        add(Dialog(title = "Test 2", lastDialogTime = System.currentTimeMillis()))
        add(Dialog(title = "Test 3", lastDialogTime = System.currentTimeMillis()))
        add(Dialog(id = 1, title = "Test 4", lastDialogTime = System.currentTimeMillis()))
    }, currentDialog, onButtonClick = { }, {}, {}, {})
}
