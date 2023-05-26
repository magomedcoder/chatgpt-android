package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFE2F4E4)
@Composable
fun AboutDialog(onDismiss: () -> Unit = {}) {
    val uriHandler = LocalUriHandler.current
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        Surface(shape = RoundedCornerShape(12.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "ChatGPT - это приложение, которое демонстрирует использование OpenAI Q&A API.",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Button(
                        onClick = {
                            uriHandler.openUri("https://github.com/magomedcoder/chatgpt-android")
                        },
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .height(28.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Посмотреть исходный код на GitHub",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AboutDialogPreview() {
    AboutDialog()
}