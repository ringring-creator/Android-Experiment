package com.ring.ring.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SIGN_UP_ROUTE = "SignUpRoute"

fun NavGraphBuilder.signUpScreen(
    popBackStack: () -> Unit,
) {
    composable(SIGN_UP_ROUTE) {
        SignUpScreen(
            toLoginScreen = popBackStack,
        )
    }
}

@Composable
internal fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    toLoginScreen: () -> Unit,
) {
    SignUpScreen(
        uiState = rememberSignUpUiState(viewModel = viewModel),
        updater = toUpdater(viewModel),
        toLoginScreen = toLoginScreen,
    )

    LaunchedEffect(Unit) {
        viewModel.signUpFinishedEvent.collect {
            toLoginScreen()
        }
    }
}

@Composable
private fun rememberSignUpUiState(
    viewModel: SignUpViewModel,
): SignUpUiState {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    return SignUpUiState(
        email = email,
        password = password,
    )
}

private fun toUpdater(viewModel: SignUpViewModel) = SignUpUiUpdater(
    setEmail = viewModel::setEmail,
    setPassword = viewModel::setPassword,
    signUp = viewModel::signUp,
)

data class SignUpUiState(
    val email: String,
    val password: String,
)

data class SignUpUiUpdater(
    val setEmail: (email: String) -> Unit,
    val setPassword: (password: String) -> Unit,
    val signUp: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpScreen(
    uiState: SignUpUiState,
    updater: SignUpUiUpdater,
    toLoginScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sign_up)) },
                navigationIcon = {
                    IconButton(onClick = toLoginScreen) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            updater = updater,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: SignUpUiState,
    updater: SignUpUiUpdater,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmailTextField(uiState.email, updater.setEmail)
        PasswordTextField(uiState.password, updater.setPassword)
        Spacer(Modifier.height(8.dp))
        SignUpButton(updater.signUp)
    }
}

@Composable
private fun EmailTextField(
    email: String,
    setEmail: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = setEmail,
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    )
}

@Composable
private fun PasswordTextField(
    password: String,
    setPassword: (String) -> Unit,
) {
    OutlinedTextField(
        value = password,
        onValueChange = setPassword,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun SignUpButton(signUp: () -> Unit) {
    Button(
        onClick = signUp,
        modifier = Modifier.fillMaxWidth()
    ) { Text(stringResource(R.string.sign_up)) }
}

@Preview
@Composable
private fun Preview() {
    PreviewSignUpScreen()
}