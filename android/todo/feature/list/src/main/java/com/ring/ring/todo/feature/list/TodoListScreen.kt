@file:OptIn(ExperimentalMaterial3Api::class)

package com.ring.ring.todo.feature.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val TODO_LIST_ROUTE = "TodoListRoute"

fun NavGraphBuilder.todoListScreen(
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
) {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen(
            toCreateTodoScreen = toCreateTodoScreen,
            toEditTodoScreen = toEditTodoScreen,
        )
    }
}

@Composable
internal fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel(),
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState = rememberTodoListUiState(viewModel = viewModel)
    val updater = toUpdater(viewModel = viewModel)

    TodoListScreen(
        uiState = uiState,
        updater = updater,
        toCreateTodoScreen = toCreateTodoScreen,
        toEditTodoScreen = toEditTodoScreen,
        snackBarHostState = snackBarHostState,
    )

    SetupSideEffect(viewModel, snackBarHostState)
}

@Composable
private fun SetupSideEffect(
    viewModel: TodoListViewModel,
    snackBarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        viewModel.fetchTodoList()
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchErrorEvent.collect {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_get_todo_list),
                actionLabel = context.getString(R.string.retry),
                withDismissAction = true,
            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> viewModel.fetchTodoList()
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.toggleDoneErrorEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_edit_done),
                withDismissAction = true,
            )
        }
    }
}

@Composable
internal fun TodoListScreen(
    uiState: TodoListUiState,
    updater: TodoListUiUpdater,
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
//    toMyPageScreen: () -> Unit,
    snackBarHostState: SnackbarHostState,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { DrawerSheet() }
        },
    ) {
        Scaffold(
            topBar = { TopBar(scope, drawerState) },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = toCreateTodoScreen) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            },
        ) {
            val modifier = Modifier.padding(it)
            Content(
                modifier, uiState,
                toEditTodoScreen,
                updater,
            )
        }
    }
}

@Composable
private fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.todo_list),
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.clickable {
                    scope.launch { drawerState.open() }
                }
            )
        },
        modifier = Modifier.padding(start = 8.dp)
    )
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
    uiState: TodoListUiState,
    toEditTodoScreen: (id: String) -> Unit,
    updater: TodoListUiUpdater,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        uiState.todoList.forEach { todo ->
            Item(
                toEditTodoScreen,
                todo,
                updater,
            )
        }
    }
}

@Composable
private fun Item(
    toEditTodoScreen: (id: String) -> Unit,
    todo: TodoListUiState.Todo,
    updater: TodoListUiUpdater
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { toEditTodoScreen(todo.id.toString()) }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DoneCheckBox(todo.done) {
                updater.toggleDone(todo.id)
            }
            TitleText(todo.title)
            Spacer(modifier = Modifier.weight(1f))
        }
        DeadlineText(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            deadline = todo.deadline,
        )
    }
}

@Composable
private fun DoneCheckBox(
    done: Boolean,
    onCheckedChange: () -> Unit,
) {
    Checkbox(
        checked = done,
        onCheckedChange = { onCheckedChange() },
        modifier = Modifier.padding(end = 8.dp)
    )
}

@Composable
private fun TitleText(title: String) {
    Text(title, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun DeadlineText(modifier: Modifier, deadline: String) {
    Text(
        "Deadline: $deadline",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}
