package ru.magomedcoder.chatgpt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import ru.magomedcoder.chatgpt.R.raw

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFE2F4E4)
@Composable
fun EmptyMessage() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val compositionResult: LottieCompositionResult = rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(raw.empty_message)
            )
            val progressAnimation by animateLottieCompositionAsState(
                compositionResult.value,
                isPlaying = true,
                iterations = LottieConstants.IterateForever,
                speed = 0.5f
            )
            LottieAnimation(
                composition = compositionResult.value,
                progress = progressAnimation,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}