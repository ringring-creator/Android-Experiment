package com.ring.ring.todo.feature.edit


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EditTodoNav {
    val route = "EditTodoRoute/{$ID}"

    companion object {
        const val ID = "id"
        fun editRoute(id: String) = "EditTodoRoute/$id"
    }
}

fun NavGraphBuilder.editTodoScreen(
    toTodoListScreen: () -> Unit,
) {
    composable(
        route = EditTodoNav().route,
        arguments = listOf(navArgument(EditTodoNav.ID) {
            type = NavType.LongType
        }),
    ) {
        EditTodoScreen(toTodoListScreen = toTodoListScreen)
    }
}

@Composable
internal fun EditTodoScreen(
    viewModel: EditTodoViewModel = hiltViewModel(),
    toTodoListScreen: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    EditTodoScreen(
        uiState = rememberEditTodoUiState(viewModel = viewModel),
        updater = toEditTodoUiUpdater(viewModel = viewModel),
        toTodoListScreen = toTodoListScreen,
        snackBarHostState = snackBarHostState,
    )

    SetupSideEffect(viewModel, toTodoListScreen, snackBarHostState)
}

@Composable
private fun SetupSideEffect(
    viewModel: EditTodoViewModel,
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        viewModel.getTodo()
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.editFinishedEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.success_to_edit),
                withDismissAction = true,
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.deleteFinishedEvent.collect {
            toTodoListScreen()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.editErrorEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_edit),
                withDismissAction = true,
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.deleteErrorEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_delete),
                withDismissAction = true,
            )
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getTodoErrorEvent.collect {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_get_todo_list),
                actionLabel = "Retry",
                withDismissAction = true,
            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {
                    viewModel.getTodo()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditTodoScreen(
    uiState: EditTodoUiState,
    updater: EditTodoUiUpdater,
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_todo)) },
                navigationIcon = {
                    IconButton(onClick = toTodoListScreen) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { padding ->
        Content(
            Modifier
                .padding(padding)
                .padding(16.dp), uiState, updater
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier,
    uiState: EditTodoUiState,
    updater: EditTodoUiUpdater,
    datePickerState: DatePickerState = rememberDatePickerState(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TitleTextField(uiState.title, updater.setTitle)
            DescriptionTextField(uiState.description, updater.setDescription)
            DoneCheckBox(uiState.done, updater.setDone)
            DeadlineField(uiState.deadline.formatString(), updater.showDatePicker)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EditButton { scope.launch { updater.editTodo() } }
                DeleteButton { scope.launch { updater.deleteTodo() } }
            }
        }
        CustomDatePicker(
            uiState.isShowDatePicker,
            datePickerState,
            updater.dismissDatePicker,
            updater.setDeadline,
        )
    }
}

@Composable
private fun TitleTextField(
    title: String,
    setTitle: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = setTitle,
        label = { Text(stringResource(R.string.title)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DescriptionTextField(
    description: String,
    setDescription: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = setDescription,
        label = { Text(stringResource(R.string.description)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DoneCheckBox(
    done: Boolean,
    setDone: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = done,
            onCheckedChange = setDone,
        )
        Text(stringResource(R.string.done))
    }
}

@Composable
private fun DeadlineField(
    deadline: String,
    showDatePicker: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = showDatePicker),
    ) {
        Icon(
            Icons.Filled.DateRange,
            contentDescription = null,
        )
        Text(deadline)
    }
}

@Composable
private fun EditButton(
    edit: () -> Unit,
) {
    Button(
        onClick = edit,
    ) { Text(stringResource(R.string.edit)) }
}

@Composable
private fun DeleteButton(
    delete: () -> Unit,
) {
    Button(
        onClick = delete,
    ) { Text(stringResource(R.string.delete)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePicker(
    isShowDatePicker: Boolean,
    datePickerState: DatePickerState,
    dismissDatePicker: () -> Unit,
    setDate: (Long) -> Unit,
) {
    if (isShowDatePicker) {
        DatePickerDialog(
            onDismissRequest = dismissDatePicker,
            confirmButton = {
                Text("Set", modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        datePickerState.selectedDateMillis?.let { setDate(it) }
                        dismissDatePicker()
                    })
            }
        ) {
            DatePicker(datePickerState)
        }
    }
}