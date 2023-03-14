package ru.magomedcoder.chatopenai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.magomedcoder.chatopenai.ui.theme.ChatOpenAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatOpenAITheme {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatOpenAITheme {}
}