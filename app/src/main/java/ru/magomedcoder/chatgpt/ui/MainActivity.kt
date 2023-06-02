package ru.magomedcoder.chatgpt.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.magomedcoder.chatgpt.ui.screen.ChatScreen
import ru.magomedcoder.chatgpt.ui.screen.ChatViewModel
import ru.magomedcoder.chatgpt.ui.theme.ChatGPTTheme
import ru.magomedcoder.chatgpt.utils.VoiceToTextParser

class MainActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModel()

    private val voiceToText by lazy {
        VoiceToTextParser(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 201
            )
        }
        setContent {
            ChatGPTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MaterialTheme.colorScheme.primary
                    ChatScreen(viewModel, voiceToText)
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

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}