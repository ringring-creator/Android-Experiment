package com.ring.ring.user.feature.signup

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.user.feature.signup.SignUpEvent.SignUpError
import com.ring.ring.user.feature.signup.SignUpEvent.SignUpSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    toLoginScreen: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    SignUpScreen(
        uiState = rememberSignUpUiState(viewModel = viewModel),
        updater = remember { toUpdater(viewModel) },
        snackBarHostState = snackBarHostState,
        toLoginScreen = toLoginScreen,
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                SignUpSuccess -> toLoginScreen()
                SignUpError -> showSignUpFailedSnackbar(snackBarHostState, context, scope)
            }
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
            email = uiState.email,
            password = uiState.password,
            updater = updater,
        )
    }
}

private suspend fun showSignUpFailedSnackbar(
    snackBarHostState: SnackbarHostState,
    context: Context,
    scope: CoroutineScope
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = context.getString(R.string.failed_to_sign_up),
            withDismissAction = true,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    email: SignUpUiState.Email,
    password: SignUpUiState.Password,
    updater: SignUpUiUpdater,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EmailTextField(email, updater.setEmail)
        PasswordTextField(password, updater.setPassword)
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
        value = email.value,
        onValueChange = setEmail,
        label = { Text(stringResource(R.string.email)) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("EmailTextField"),
        isError = email.isError,
        supportingText = {
            if (email.isShowSupportingText) {
                Text(stringResource(R.string.email_is_already_registered))
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
        value = password.value,
        onValueChange = setPassword,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        isError = password.isError,
        supportingText = {
            Text(stringResource(R.string.password_is_invalid))
        },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("PasswordTextField"),
    )
}

@Composable
private fun SignUpButton(signUp: () -> Unit) {
    Button(
        onClick = signUp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("SignUpButton")
    ) { Text(stringResource(R.string.sign_up)) }
}