package ru.magomedcoder.chatgpt.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.get
import ru.magomedcoder.chatgpt.R
import ru.magomedcoder.chatgpt.ui.theme.Purple40

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {

    val list by viewModel.list.observeAsState(emptyList())

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topR, listR, bottomR) = createRefs()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topR) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(Purple40)
                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier.padding(10.dp))
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                modifier = Modifier
                    .clickable(interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            navController.navigate("chat?id=0")
                        })
                    .padding(10.dp)
            )
        }
        Box(modifier = Modifier
            .constrainAs(listR) {
                top.linkTo(topR.bottom)
                bottom.linkTo(bottomR.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(top = 5.dp, bottom = 5.dp)
        ) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    HomeScreen(navController, get())
}