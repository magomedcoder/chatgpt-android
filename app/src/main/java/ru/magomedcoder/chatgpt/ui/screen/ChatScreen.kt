package ru.magomedcoder.chatgpt.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.Constants
import ru.magomedcoder.chatgpt.domain.model.Dialog
import ru.magomedcoder.chatgpt.domain.model.VolumeState
import ru.magomedcoder.chatgpt.ui.components.AppTopBar
import ru.magomedcoder.chatgpt.ui.components.ChatInput
import ru.magomedcoder.chatgpt.ui.components.KeyDialog
import ru.magomedcoder.chatgpt.ui.components.MessageList
import ru.magomedcoder.chatgpt.ui.components.SideBar
import ru.magomedcoder.chatgpt.ui.theme.Purple40
import ru.magomedcoder.chatgpt.utils.VoiceToTextParser
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel, voiceToText: VoiceToTextParser) {
    val ctx = LocalContext.current
    val dialogs by viewModel.dialogList.observeAsState(emptyList())
    val list by viewModel.list.observeAsState(emptyList())
    val volumeState by viewModel.volumeState.observeAsState(VolumeState())
    val currentDialog by viewModel.currentDialog.observeAsState(
        Dialog(
            title = "", lastDialogTime = System.currentTimeMillis()
        )
    )
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var isVisible by remember { mutableStateOf(false) }
    var isScreen by remember { mutableStateOf(false) }
    var isShowEditKey by remember { mutableStateOf(false) }
    val voiceToTextParserState by voiceToText.state.collectAsState()
    if (Constants.GlobalConfig.apiKey.isEmpty()) {
        isShowEditKey = true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple40)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topR, listR, bottomR) = createRefs()
            AppTopBar(
                currentDialog,
                onVisibleSideBar = { isVisible = !isVisible },
                onDeleteClick = {
                    viewModel.deleteCurrentDialog()
                },
                modifier = Modifier.constrainAs(topR) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            MessageList(ctx, list, viewModel, volumeState, modifier = Modifier.constrainAs(listR) {
                top.linkTo(topR.bottom)
                bottom.linkTo(bottomR.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }, onShowEditKeyClick = {
                isShowEditKey = true
            })
            val keyboardController = LocalSoftwareKeyboardController.current
            ConstraintLayout(modifier = Modifier.constrainAs(bottomR) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
                end.linkTo(parent.end)
            }) {
                val (textR, sendR) = createRefs()
                ChatInput(value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.constrainAs(textR) {
                        start.linkTo(parent.start)
                        end.linkTo(sendR.start)
                        width = Dimension.fillToConstraints
                    },
                    onClick = {
                        keyboardController?.hide()
                        viewModel.sendMessage(text.text)
                        text = TextFieldValue("")
                    },
                    onRecordingClick = {
                        if (!voiceToTextParserState.isSpeaking) {
                            voiceToText.startListening("ru")
                        } else {
                            voiceToText.stopListening()
                            if(voiceToTextParserState.spokenText.isNotEmpty()){
                                viewModel.sendMessage(text.text)
                            }
                        }
                    },
                    voiceToTextParserState = voiceToTextParserState
                )
            }
        }
        AnimatedVisibility(
            visible = isVisible, enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ), exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        ) {
            SideBar(list = dialogs, currentDialog = currentDialog, onButtonClick = {
                viewModel.startNewDialog()
                isVisible = !isVisible
            }, onItemClick = { dialog ->
                viewModel.switchDialog(dialog)
                isVisible = !isVisible
            },
                onDismiss = {
                    isVisible = !isVisible
                },
                onScreen = {
                    isScreen = !isScreen
                }
            )
        }
    }
    if (isShowEditKey) {
        KeyDialog(onConfirm = {
            Constants.GlobalConfig.apiKey = it
            isShowEditKey = false
        }, onDismiss = {})
    }
    AnimatedVisibility(
        visible = isScreen, enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ), exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    ) {
        ImageScreen(get(), onScreen = { isScreen = !isScreen })
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatScreen(get(), get())
}