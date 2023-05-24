package ru.magomedcoder.chatgpt.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.ui.components.AppTopBar
import ru.magomedcoder.chatgpt.ui.components.ChatList
import ru.magomedcoder.chatgpt.ui.components.EmptyDialog

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val list by viewModel.list.observeAsState(emptyList())
    Scaffold(
        topBar = { AppTopBar(navController) },
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (list.isNotEmpty()) {
                ChatList(list, navController)
            } else {
                EmptyDialog()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    HomeScreen(navController, get())
}