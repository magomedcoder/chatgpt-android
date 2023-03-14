package ru.magomedcoder.chatopenai.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.magomedcoder.chatopenai.ui.theme.ChatOpenAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatOpenAITheme {}
        }
    }

}
