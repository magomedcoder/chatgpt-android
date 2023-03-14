package ru.magomedcoder.chatopenai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ChatOpenAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Purple40,
            primary = Purple40
        ),
        typography = Typography,
        content = content
    )
}