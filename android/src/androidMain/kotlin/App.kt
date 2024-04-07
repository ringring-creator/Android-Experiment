import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ring.ring.login.LoginScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        LoginScreen({}, {})
    }
}