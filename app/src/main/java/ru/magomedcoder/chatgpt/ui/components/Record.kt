package ru.magomedcoder.chatgpt.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.ui.theme.Purple80
import ru.magomedcoder.chatgpt.utils.VoiceToTextParser

@Composable
fun Record(voiceToText: VoiceToTextParser) {
    val state by voiceToText.state.collectAsState()
    var canRecord by remember {
        mutableStateOf(false)
    }
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )
    LaunchedEffect(key1 = recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Purple80,
                onClick = {
                    if (canRecord) {
                        if (!state.isSpeaking) {
                            voiceToText.startListening("ru")
                        } else {
                            voiceToText.stopListening()
                        }
                    }
                }
            ) {
                AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                    if (isSpeaking) {
                        Icon(
                            imageVector = Icons.Rounded.Stop,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Mic,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                if (isSpeaking) {
                    Text(
                        text = "Скажите что-нибудь",
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    Text(
                        text = state.spokenText.ifEmpty { "Нажмите на запись" },
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordPreview() {
    Record(get())
}