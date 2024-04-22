package com.ring.ring.user.feature.login.view

import android.content.Context
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.user.feature.login.R
import com.ring.ring.user.feature.login.viewmodel.LoginEvent
import com.ring.ring.user.feature.login.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun LoginScreen(
    toTodoListScreen: () -> Unit,
    toSignUpScreen: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: LoginViewModel = hiltViewModel(),
) {

    LoginScreen(
        uiState = rememberLoginUiState(viewModel),
        updater = remember { toUpdater(viewModel) },
        snackBarHostState = snackBarHostState,
        toSignUpScreen = toSignUpScreen,
    )

    SetupSideEffect(viewModel, toTodoListScreen, snackBarHostState)
}

@Composable
private fun SetupSideEffect(
    viewModel: LoginViewModel,
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                LoginEvent.LoginSuccess -> toTodoListScreen()
                LoginEvent.LoginError -> showLoginFailedSnackbar(snackBarHostState, context, scope)
            }
        }
    }
}

private suspend fun showLoginFailedSnackbar(
    snackBarHostState: SnackbarHostState,
    context: Context,
    scope: CoroutineScope
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = context.getString(R.string.failed_to_login),
            withDismissAction = true,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    updater: LoginUiUpdater,
    snackBarHostState: SnackbarHostState,
    toSignUpScreen: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.login)) }) },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            email = uiState.email,
            password = uiState.password,
            updater = updater,
            toSignUpScreen = toSignUpScreen
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    email: String,
    password: String,
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
        EmailTextField(email, updater.setEmail)
        PasswordTextField(password, updater.setPassword)
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
        modifier = Modifier
            .fillMaxWidth()
            .testTag("EmailTextField"),
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
        modifier = Modifier
            .fillMaxWidth()
            .testTag("PasswordTextField"),
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
private fun SignUpText(toSignUpScreen: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.dont_have_an_account))
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
        modifier = Modifier
            .fillMaxWidth()
            .testTag("LoginButton"),
    ) {
        Text(stringResource(id = R.string.login))
    }
}