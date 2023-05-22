package ru.magomedcoder.chatgpt.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.ui.components.ChatTextField
import ru.magomedcoder.chatgpt.ui.components.LeftView
import ru.magomedcoder.chatgpt.ui.components.RightView
import ru.magomedcoder.chatgpt.ui.theme.Purple80
import ru.magomedcoder.chatgpt.utils.enums.Role

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
            if (list.isNotEmpty()) {
                LaunchedEffect(key1 = list) {
                    scrollState.animateScrollToItem(list.size - 1)
                }
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