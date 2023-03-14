package ru.magomedcoder.chatopenai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RightView(content: String) {
    Row(
        modifier = Modifier.padding(end = 5.dp, start = 60.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            modifier = Modifier.padding(end = 7.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF222222)
            )
        ) {
            Text(
                text = content, color = Color.White, modifier = Modifier.padding(10.dp)
            )
        }
    }
}
