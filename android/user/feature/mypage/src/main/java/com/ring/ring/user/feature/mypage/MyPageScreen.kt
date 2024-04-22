@file:OptIn(ExperimentalMaterial3Api::class)

package com.ring.ring.user.feature.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
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
import com.ring.ring.mypage.R
import com.ring.ring.user.feature.mypage.MyPageEvent.EditError
import com.ring.ring.user.feature.mypage.MyPageEvent.EditSuccess
import com.ring.ring.user.feature.mypage.MyPageEvent.LogoutError
import com.ring.ring.user.feature.mypage.MyPageEvent.LogoutSuccess
import com.ring.ring.user.feature.mypage.MyPageEvent.UnauthorizedError
import com.ring.ring.user.feature.mypage.MyPageEvent.WithdrawalError
import com.ring.ring.user.feature.mypage.MyPageEvent.WithdrawalSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    toLoginScreen: () -> Unit,
    toTodoListScreen: () -> Unit,
    toMyPageScreen: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    MyPageScreen(
        uiState = rememberSignUpUiState(viewModel),
        updater = remember { toUpdater(viewModel) },
        toTodoListScreen = toTodoListScreen,
        toMyPageScreen = toMyPageScreen,
        snackbarHostState = snackbarHostState,
    )

    SetupSideEffect(viewModel, toLoginScreen, snackbarHostState)
}

@Composable
internal fun MyPageScreen(
    uiState: MyPageUiState,
    updater: MyPageUiUpdater,
    toTodoListScreen: () -> Unit,
    toMyPageScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    val scope: CoroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerSheet(
                    toTodoListScreen = toTodoListScreen,
                    toMyPageScreen = toMyPageScreen,
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    expanded = uiState.expandedAction,
                    updater = updater,
                    scope = scope,
                    drawerState = drawerState
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Content(
                    email = uiState.email,
                    password = uiState.password,
                    updater = updater,
                )
                WithdrawalDialog(
                    showDialog = uiState.showDialog,
                    setShowDialog = updater.setShowDialog,
                    withdrawal = updater.withdrawal,
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    expanded: Boolean,
    updater: MyPageUiUpdater,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.my_page),
                style = MaterialTheme.typography.headlineLarge
            )
        },
        modifier = Modifier.padding(start = 8.dp),
        navigationIcon = {
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.clickable {
                    scope.launch { drawerState.open() }
                }
            )
        },
        actions = {
            ActionMenu(
                expanded = expanded,
                setExpanded = updater.setExpandedAction,
                logout = updater.logout,
            )
        }
    )
}

@Composable
fun ActionMenu(
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    logout: () -> Unit,
) {
    Box {
        IconButton(
            onClick = { setExpanded(true) },
            modifier = Modifier.testTag("ActionMenuIcon")
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.logout)) },
                onClick = logout,
                modifier = Modifier.testTag("LogoutDropdownMenu")
            )
        }
    }
}

@Composable
private fun DrawerSheet(
    toTodoListScreen: () -> Unit,
    toMyPageScreen: () -> Unit,
) {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
        label = { Text(stringResource(R.string.todo)) },
        selected = false,
        onClick = toTodoListScreen,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Person, contentDescription = null) },
        label = { Text(stringResource(R.string.my_page)) },
        selected = true,
        onClick = toMyPageScreen,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
private fun SetupSideEffect(
    viewModel: MyPageViewModel,
    toLoginScreen: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                EditSuccess -> showSnackbar(
                    snackbarHostState,
                    context.getString(R.string.success_to_edit),
                    scope,
                )

                EditError -> showSnackbar(
                    snackbarHostState,
                    context.getString(R.string.failed_to_edit),
                    scope,
                )

                WithdrawalError -> showSnackbar(
                    snackbarHostState,
                    context.getString(R.string.failed_to_withdrawal),
                    scope,
                )

                WithdrawalSuccess, UnauthorizedError, LogoutSuccess, LogoutError -> toLoginScreen()
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getUser()
    }
}

@Composable
private fun WithdrawalDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    withdrawal: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = {
                Text(text = stringResource(R.string.withdrawal_confirmation))
            },
            text = {
                Text(text = stringResource(R.string.withdrawal_confirmation_content))
            },
            confirmButton = {
                Button(onClick = withdrawal, modifier = Modifier.testTag("ConfirmButton")) {
                    Text(text = stringResource(R.string.withdrawal_confirmation_yes))
                }
            },
            dismissButton = {
                Button(onClick = { setShowDialog(false) }) {
                    Text(text = stringResource(R.string.withdrawal_confirmation_no))
                }
            }
        )
    }
}

@Composable
private fun Content(
    email: MyPageUiState.Email,
    password: MyPageUiState.Password,
    updater: MyPageUiUpdater,
) {
    Column(
        modifier = Modifier
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
            WithdrawalButton { updater.setShowDialog(true) }
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
    showDialog: () -> Unit,
) {
    Button(
        onClick = showDialog,
        modifier = Modifier.testTag("WithdrawalButton")
    ) { Text(stringResource(R.string.withdrawal)) }
}