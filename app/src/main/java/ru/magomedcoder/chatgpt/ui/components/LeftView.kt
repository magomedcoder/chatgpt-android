package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LeftView(content: String) {
    Row(modifier = Modifier.padding(start = 10.dp, end = 60.dp)) {
        SelectionContainer() {
            Card(
                modifier = Modifier.padding(start = 7.dp), colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF222222)
                )
            ) {
                if (content.isEmpty()) {
                    DotsTyping(
                        modifier = Modifier.padding(15.dp)
                    )
                } else {
                    Text(
                        text = content, color = Color.White, modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }
    }
}
