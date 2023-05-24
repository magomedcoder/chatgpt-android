package ru.magomedcoder.chatgpt.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.ui.components.ChatTextField
import ru.magomedcoder.chatgpt.ui.components.EmptyMessage
import ru.magomedcoder.chatgpt.ui.components.MessageList
import ru.magomedcoder.chatgpt.ui.theme.Purple80

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(navController: NavHostController, dialogId: Int, viewModel: ChatViewModel) {

    val list by viewModel.list.observeAsState(emptyList())
    var text by remember { mutableStateOf(TextFieldValue("")) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topR, listR, bottomR) = createRefs()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topR) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            navController.navigate("home")
                        })
                    .padding(10.dp)
            )
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                modifier = Modifier
                    .clickable(interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            viewModel.clear()
                        })
                    .padding(10.dp)
            )
        }
        Box(modifier = Modifier
            .constrainAs(listR) {
                top.linkTo(topR.bottom)
                bottom.linkTo(bottomR.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
            .background(Purple80)) {
            if (list.isNotEmpty()) {
                val scrollState = rememberLazyListState()
                MessageList(list)
                if (list.isNotEmpty()) {
                    LaunchedEffect(key1 = list) {
                        scrollState.animateScrollToItem(list.size - 1)
                    }
                }
            } else {
                EmptyMessage()
            }
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        ConstraintLayout(modifier = Modifier.constrainAs(bottomR) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            width = Dimension.fillToConstraints
            end.linkTo(parent.end)
        }) {
            val (textR, sendR) = createRefs()
            ChatTextField(value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .constrainAs(textR) {
                        start.linkTo(parent.start)
                        end.linkTo(sendR.start)
                        width = Dimension.fillToConstraints
                    }
                    .padding(5.dp),
                onClick = {
                    keyboardController?.hide()
                    viewModel.sendMessage(text.text)
                    text = TextFieldValue("")
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    ChatScreen(navController, 0, get())
}