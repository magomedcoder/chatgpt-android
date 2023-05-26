package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import ru.magomedcoder.chatgpt.R
import ru.magomedcoder.chatgpt.domain.model.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentDialog: Dialog,
    onVisibleSideBar: () -> Unit,
    onDeleteClick: (Dialog) -> Unit,
    modifier: Modifier,
) {
    var isOpenPop by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val scrollState = rememberScrollState(0)
                Box(modifier = Modifier.horizontalScroll(scrollState)) {
                    Text(
                        text = currentDialog.title.ifEmpty { stringResource(id = R.string.app_name) },
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(16)
                        if (scrollState.value == scrollState.maxValue) {
                            delay(1000)
                            scrollState.scrollTo(0)
                            delay(1000)
                        } else {
                            scrollState.scrollBy(1f)
                        }
                    }
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onVisibleSideBar) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        actions = {
            IconButton(onClick = {
                isOpenPop = !isOpenPop
            }) {
                Icon(Icons.Filled.MoreVert, "Info")
            }
        }
    )
    if (isOpenPop) {
        Popup(
            offset = IntOffset(0, 150),
            alignment = Alignment.TopEnd,
            onDismissRequest = { isOpenPop = false },
            content = {
                Card() {
                    Column(modifier = Modifier.width(200.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDeleteClick.invoke(currentDialog)
                                    isOpenPop = false
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton({}) {
                                Icon(Icons.Filled.Delete, contentDescription = null)
                            }
                            Text(text = "Удалить")
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun AppTopBarPreview() {
    val currentDialog = Dialog(id = 1, title = "Test", lastDialogTime = System.currentTimeMillis())
    AppTopBar(currentDialog, onVisibleSideBar = {}, onDeleteClick = {}, Modifier)
}