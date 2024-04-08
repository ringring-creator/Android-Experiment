import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ring.ring.login.LOGIN_ROUTE
import com.ring.ring.login.loginScreen

@Composable
fun App(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = LOGIN_ROUTE,
    ) {
        loginScreen(
            toTodoListScreen = {},
            toSignUpScreen = {},
        )
    }
}