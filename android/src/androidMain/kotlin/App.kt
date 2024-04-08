import androidx.compose.runtime.Composable
import com.ring.ring.login.LoginScreen
import com.ring.ring.theme.AndroidExperimentTheme

@Composable
fun App() {
    AndroidExperimentTheme {
        LoginScreen({}, {})
    }
}