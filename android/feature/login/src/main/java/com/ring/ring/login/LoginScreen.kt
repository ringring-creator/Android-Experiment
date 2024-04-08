package com.ring.ring.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val LOGIN_ROUTE = "LoginRoute"

fun NavGraphBuilder.loginScreen(
    toTodoListScreen: () -> Unit,
    toSignUpScreen: () -> Unit,
) {
    composable(LOGIN_ROUTE) {
        LoginScreen(
            toTodoListScreen = toTodoListScreen,
            toSignUpScreen = toSignUpScreen,
        )
    }
}


@Composable
internal fun LoginScreen(
    toTodoListScreen: () -> Unit,
    toSignUpScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState = rememberLoginUiState(viewModel)
    val updater = toUpdater(viewModel)

    LoginScreen(
        uiState = uiState,
        updater = updater,
        toSignUpScreen = toSignUpScreen,
    )

    LaunchedEffect(Unit) {
        viewModel.loginFinishedEvent.collect {
            toTodoListScreen()
        }
    }
}

private fun toUpdater(viewModel: LoginViewModel) = LoginUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    login = viewModel::login,
)

@Composable
private fun rememberLoginUiState(viewModel: LoginViewModel): LoginUiState {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    return LoginUiState(
        email = email,
        password = password,
    )
}

data class LoginUiState(
    val email: String,
    val password: String,
)

data class LoginUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val login: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    updater: LoginUiUpdater,
    toSignUpScreen: () -> Unit,
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
        EmailTextField(uiState.email, updater.setEmail)
        PasswordTextField(uiState.password, updater.setPassword)
        Spacer(Modifier.height(8.dp))
        LoginButton { updater.login() }
        Spacer(Modifier.height(16.dp))
        SignUpText(toSignUpScreen = toSignUpScreen)
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

@Preview
@Composable
private fun Preview() {
    PreviewLoginScreen()
}