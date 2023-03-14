import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftView(content: String) {
    Row(modifier = Modifier.padding(start = 5.dp, end = 60.dp)) {
        Card(
            modifier = Modifier.padding(start = 7.dp),
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
