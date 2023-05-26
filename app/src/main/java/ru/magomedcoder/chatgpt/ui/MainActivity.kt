package ru.magomedcoder.chatgpt.ui

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.magomedcoder.chatgpt.ui.screen.ChatScreen
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.ui.theme.ChatGPTTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatGPTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MaterialTheme.colorScheme.primary
                    ChatScreen(viewModel)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                viewModel.setVolumeState(touchDown = true)
                return true
            }

            KeyEvent.KEYCODE_VOLUME_UP -> {
                viewModel.setVolumeState(touchUp = true)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}