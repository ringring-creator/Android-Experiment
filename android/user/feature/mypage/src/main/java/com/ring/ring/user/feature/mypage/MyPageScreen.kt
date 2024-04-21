package com.ring.ring.user.feature.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.mypage.R

@Composable
internal fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    MyPageScreen(
        uiState = rememberSignUpUiState(viewModel),
        updater = remember { toUpdater(viewModel) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyPageScreen(
    uiState: MyPageUiState,
    updater: MyPageUiUpdater,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheet { DrawerSheet() } },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.my_page)) },
                )
            },
        ) { paddingValues ->
            Content(
                modifier = Modifier.padding(paddingValues),
                email = uiState.email,
                password = uiState.password,
                updater = updater,
            )
        }
    }
}

@Composable
private fun DrawerSheet() {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
        label = { Text(stringResource(R.string.todo)) },
        selected = true,
        onClick = {},
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Person, contentDescription = null) },
        label = { Text(stringResource(R.string.my_page)) },
        selected = false,
        onClick = {},
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    email: MyPageUiState.Email,
    password: MyPageUiState.Password,
    updater: MyPageUiUpdater,
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
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EditButton(updater.edit)
            WithdrawalButton(updater.withdrawal)
        }
    }
}

@Composable
private fun EmailTextField(
    email: MyPageUiState.Email,
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
    password: MyPageUiState.Password,
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
private fun EditButton(
    edit: () -> Unit,
) {
    Button(
        onClick = edit,
        modifier = Modifier.testTag("EditButton")
    ) { Text(stringResource(R.string.edit)) }
}

@Composable
private fun WithdrawalButton(
    withdrawal: () -> Unit,
) {
    Button(
        onClick = withdrawal,
        modifier = Modifier.testTag("WithdrawalButton")
    ) { Text(stringResource(R.string.withdrawal)) }
}