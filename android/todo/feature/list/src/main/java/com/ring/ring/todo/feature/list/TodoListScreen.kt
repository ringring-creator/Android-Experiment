package com.ring.ring.todo.feature.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val TODO_LIST_ROUTE = "TodoListRoute"

fun NavGraphBuilder.todoListScreen(
    toCreateTodoScreen: () -> Unit,
) {
    composable(TODO_LIST_ROUTE) {
        TodoListScreen(
            toCreateTodoScreen = toCreateTodoScreen,
        )
    }
}


@Composable
internal fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel(),
    toCreateTodoScreen: () -> Unit,
) {
    val uiState = rememberTodoListUiState(viewModel = viewModel)
    val updater = toUpdater(viewModel = viewModel)

    TodoListScreen(
        uiState = uiState,
        updater = updater,
        toCreateTodoScreen = toCreateTodoScreen,
    )

    LaunchedEffect(Unit) {
        viewModel.fetchTodoList()
    }
}

@Composable
internal fun TodoListScreen(
    uiState: TodoListUiState,
    updater: TodoListUiUpdater,
    toCreateTodoScreen: () -> Unit,
//    toEditTodoScreen: (Long) -> Unit,
//    toMyPageScreen: () -> Unit,
//    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
//        topBar = { TodoNavBar(toMyPageScreen) },
//        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = toCreateTodoScreen) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
        val modifier = Modifier.padding(it)
        Content(
            modifier, uiState,
//            toEditTodoScreen, scope,
            updater,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    uiState: TodoListUiState,
//    toEditTodoScreen: (Long) -> Unit,
//    scope: CoroutineScope,
    updater: TodoListUiUpdater,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        uiState.todoList.forEach { todo ->
            Item(
//                toEditTodoScreen,
                todo,
                updater,
            )
        }
    }
}

@Composable
private fun TodoNavBar(
    toMyPageScreen: () -> Unit,
) {
    NavigationBar {
        NavigationBar {
            NavigationRailItem(
                icon = { },
                label = { Text("Todo") },
                selected = true,
                onClick = {}
            )
            NavigationRailItem(
                icon = { },
                label = { Text("My Page") },
                selected = false,
                onClick = toMyPageScreen
            )
        }
    }
}

@Composable
private fun Header() {
    Text("Todo List", style = MaterialTheme.typography.headlineLarge)
}

@Composable
private fun Item(
//    toEditTodoScreen: (Long) -> Unit,
    todo: TodoListUiState.Todo,
    updater: TodoListUiUpdater
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { }
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
