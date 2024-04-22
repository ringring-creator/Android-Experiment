@file:OptIn(ExperimentalMaterial3Api::class)

package com.ring.ring.todo.feature.list.view

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.todo.feature.list.R
import com.ring.ring.todo.feature.list.viewmodel.TodoListEvent.FetchErrorEvent
import com.ring.ring.todo.feature.list.viewmodel.TodoListEvent.ToggleDoneErrorEvent
import com.ring.ring.todo.feature.list.viewmodel.TodoListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel(),
    toTodoListScreen: () -> Unit,
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
    toMyPageScreen: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    TodoListScreen(
        uiState = rememberTodoListUiState(viewModel = viewModel),
        updater = remember { toUpdater(viewModel = viewModel) },
        toTodoListScreen = toTodoListScreen,
        toCreateTodoScreen = toCreateTodoScreen,
        toEditTodoScreen = toEditTodoScreen,
        toMyPageScreen = toMyPageScreen,
        snackBarHostState = snackbarHostState,
    )

    SetupSideEffect(viewModel, snackbarHostState)
}

@Composable
internal fun TodoListScreen(
    uiState: TodoListUiState,
    updater: TodoListUiUpdater,
    toTodoListScreen: () -> Unit,
    toCreateTodoScreen: () -> Unit,
    toEditTodoScreen: (id: String) -> Unit,
    toMyPageScreen: () -> Unit,
    snackBarHostState: SnackbarHostState,
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
            topBar = { TopBar(scope, drawerState) },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            floatingActionButton = {
                CreateTodoActionButton(toCreateTodoScreen)
            },
        ) {
            Content(
                modifier = Modifier.padding(it),
                todoList = uiState.todoList,
                toEditTodoScreen = toEditTodoScreen,
                updater = updater,
            )
        }
    }
}

@Composable
private fun SetupSideEffect(
    viewModel: TodoListViewModel,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                FetchErrorEvent -> showFetchErrorSnackbar(
                    scope,
                    snackBarHostState,
                    context,
                    viewModel::fetchTodoList,
                )

                ToggleDoneErrorEvent -> showToggleDoneErrorSnackbar(
                    scope,
                    snackBarHostState,
                    context
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.fetchTodoList()
    }
}

@Composable
private fun CreateTodoActionButton(
    toCreateTodoScreen: () -> Unit
) {
    FloatingActionButton(
        onClick = toCreateTodoScreen,
        modifier = Modifier.testTag("CreateTodoActionButton"),
    ) {
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}

private fun showFetchErrorSnackbar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    context: Context,
    fetchTodoList: () -> Unit,
) {
    scope.launch {
        val snackBarResult = snackBarHostState.showSnackbar(
            message = context.getString(R.string.failed_to_get_todo_list),
            actionLabel = context.getString(R.string.retry),
            withDismissAction = true,
        )
        when (snackBarResult) {
            SnackbarResult.Dismissed -> {}
            SnackbarResult.ActionPerformed -> fetchTodoList()
        }
    }
}

private fun showToggleDoneErrorSnackbar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    context: Context
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = context.getString(R.string.failed_to_edit_done),
            withDismissAction = true,
        )
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
private fun DrawerSheet(
    toTodoListScreen: () -> Unit,
    toMyPageScreen: () -> Unit,
) {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
        label = { Text(stringResource(R.string.todo)) },
        selected = true,
        onClick = toTodoListScreen,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Person, contentDescription = null) },
        label = { Text(stringResource(R.string.my_page)) },
        selected = false,
        onClick = toMyPageScreen,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    todoList: List<TodoListUiState.Todo>,
    toEditTodoScreen: (id: String) -> Unit,
    updater: TodoListUiUpdater,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("Content"),
    ) {
        items(todoList) { todo ->
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
            .padding(8.dp)
            .testTag("Item"),
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
        modifier = Modifier
            .padding(end = 8.dp)
            .testTag("DoneCheckBox")
    )
}

@Composable
private fun TitleText(title: String) {
    Text(title, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun DeadlineText(modifier: Modifier, deadline: String) {
    Text(
        stringResource(R.string.deadline, deadline),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.testTag("DeadlineText")
    )
}
