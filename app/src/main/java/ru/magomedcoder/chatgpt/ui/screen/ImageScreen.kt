package ru.magomedcoder.chatgpt.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.magomedcoder.chatgpt.ui.components.ImageInput
import ru.magomedcoder.chatgpt.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ImageScreen(viewModel: ImageViewModel, onScreen: (String) -> Unit) {
    val ctx = LocalContext.current
    val list by viewModel.list.observeAsState(emptyList())
    val scrollListState = rememberLazyListState()
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Column(modifier = Modifier.background(Purple40)) {
        TopAppBar(title = {
            Text(
                text = "Генерация изображений"
            )
        }, navigationIcon = {
            IconButton(onClick = {
                onScreen.invoke("")
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        })
        LazyColumn(state = scrollListState) {
            items(list.size) { position ->
                val item = list[position]
                Card(
                    modifier = Modifier.padding(10.dp), colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF020202)
                    )
                ) {
                    AsyncImage(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(item.url)
                            .crossfade(true)
                            .build(),
                        contentDescription = ""
                    )
                    Text(
                        text = item.text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    Icon(
                        Icons.Filled.Download, null,
                        Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                viewModel.download(ctx, item.url)
                            }
                    )
                }
            }
        }
    }
    ImageInput(value = text, onValueChange = { text = it }, onClick = {
        viewModel.send(text.text)
        text = TextFieldValue("")
    })
}

@Preview(showBackground = true)
@Composable
fun ImageScreenPreview() {
    ImageScreen(viewModel()) {}
}