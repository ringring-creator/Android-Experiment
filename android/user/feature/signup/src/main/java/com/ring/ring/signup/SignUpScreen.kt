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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    toLoginScreen: () -> Unit,
) {
    val context = LocalContext.current

    SignUpScreen(
        uiState = rememberSignUpUiState(viewModel = viewModel),
        updater = toUpdater(viewModel),
        snackBarHostState = snackBarHostState,
        toLoginScreen = toLoginScreen,
    )

    LaunchedEffect(Unit) {
        viewModel.signUpFinishedEvent.collect { toLoginScreen() }
    }
    LaunchedEffect(Unit) {
        viewModel.signUpFailedEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_sign_up),
                withDismissAction = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpScreen(
    uiState: SignUpUiState,
    updater: SignUpUiUpdater,
    snackBarHostState: SnackbarHostState,
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
        snackbarHost = { SnackbarHost(snackBarHostState) }
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
    email: SignUpUiState.Email,
    setEmail: (String) -> Unit
) {
    OutlinedTextField(
        value = email.input,
        onValueChange = setEmail,
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier.fillMaxWidth(),
        isError = email.isError,
        supportingText = {
            if (email.visibleSupportingText) {
                Text(stringResource(R.string.email_address_is_invalid))
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
    )
}

@Composable
private fun PasswordTextField(
    password: SignUpUiState.Password,
    setPassword: (String) -> Unit,
) {
    OutlinedTextField(
        value = password.input,
        onValueChange = setPassword,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        isError = password.isError,
        supportingText = {
            if (password.visibleSupportingText) {
                Text(stringResource(R.string.password_is_invalid))
            }
        },
        visualTransformation = PasswordVisualTransformation(),
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