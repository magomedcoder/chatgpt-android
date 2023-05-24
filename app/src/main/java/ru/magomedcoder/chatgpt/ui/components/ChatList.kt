package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.magomedcoder.chatgpt.domain.model.Dialog

@Composable
fun ChatList(list: List<Dialog> = emptyList(), navController: NavHostController) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(list.size) { position ->
            val dialog = list[position]
            val title = if (dialog.title.isNullOrEmpty()) {
                "Чат №" + (dialog.id)
            } else {
                dialog.title
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                navController.navigate("chat?id=" + dialog.id)
                            }),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2C2C2C)
                    ),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}