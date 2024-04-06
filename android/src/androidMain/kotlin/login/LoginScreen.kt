package login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ring.ring.R

data class LoginUiState(
    val email: String,
    val password: String,
)

interface LoginUiUpdater {
    fun setEmail(email: String)
    fun setPassword(password: String)
    fun login()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    updater: LoginUiUpdater,
    toSignUpScreen: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.login)) }) }
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            updater = updater,
            toSignUpScreen = toSignUpScreen
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: LoginUiState,
    updater: LoginUiUpdater,
    toSignUpScreen: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmailTextField(uiState.email, updater::setEmail)
        PasswordTextField(uiState.password, updater::setPassword)
        Spacer(Modifier.height(8.dp))
        LoginButton { updater.login() }
        Spacer(Modifier.height(16.dp))
        SignUpText({})
    }
}

@Composable
private fun EmailTextField(
    email: String,
    setEmail: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = setEmail,
        label = { Text(stringResource(id = R.string.email_address)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    )
}

@Composable
private fun PasswordTextField(
    password: String,
    setPassword: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = setPassword,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
private fun SignUpText(toSignUpScreen: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Don't have an account?")
        Button(
            onClick = { toSignUpScreen() },
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
        ) {
            Text(stringResource(R.string.sign_up))
        }
    }
}

@Composable
private fun LoginButton(login: () -> Unit) {
    Button(
        onClick = login,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(id = R.string.login))
    }
}


@Preview(showSystemUi = true, apiLevel = 34)
@Composable
private fun PreviewLoginScreen(
    @PreviewParameter(LoginScreenPreviewParameterProvider::class) uiState: LoginUiState
) {
    LoginScreen(
        uiState = uiState,
        updater = object : LoginUiUpdater {
            override fun setEmail(email: String) {}
            override fun setPassword(password: String) {}
            override fun login() {}
        }
    ) {}
}