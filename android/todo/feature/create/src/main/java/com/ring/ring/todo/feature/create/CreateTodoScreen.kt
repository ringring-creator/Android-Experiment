package com.ring.ring.todo.feature.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val CREATE_TODO_ROUTE = "CreateTodoRoute"

fun NavGraphBuilder.createTodoScreen(
    toTodoListScreen: () -> Unit,
) {
    composable(CREATE_TODO_ROUTE) {
        CreateTodoScreen(
            toTodoListScreen = toTodoListScreen
        )
    }
}


@Composable
internal fun CreateTodoScreen(
    viewModel: CreateTodoViewModel = hiltViewModel(),
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {

    CreateTodoScreen(
        uiState = rememberCreateTodoUiState(viewModel = viewModel),
        updater = toUpdater(viewModel),
        toTodoListScreen = toTodoListScreen,
        snackBarHostState = snackBarHostState,
    )

    SetupSideEffect(viewModel, toTodoListScreen, snackBarHostState)
}

@Composable
private fun SetupSideEffect(
    viewModel: CreateTodoViewModel,
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LaunchedEffect(Unit) {
        viewModel.saveSuccessEvent.collect {
            toTodoListScreen()
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.saveTodoErrorEvent.collect {
            snackBarHostState.showSnackbar(
                message = context.getString(R.string.failed_to_create),
                withDismissAction = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateTodoScreen(
    uiState: CreateTodoUiState,
    updater: CreateTodoUiUpdater,
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Todo") },
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
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            uiState = uiState,
            updater = updater,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier,
    uiState: CreateTodoUiState,
    updater: CreateTodoUiUpdater,
    datePickerState: DatePickerState = rememberDatePickerState(),
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
            DeadlineField(uiState.deadline, updater.showDatePicker)
            Spacer(modifier = Modifier.height(8.dp))
            CreateButton(updater.saveTodo)
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
        label = { Text("Title") },
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
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DoneCheckBox(
    done: Boolean,
    setDone: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = done,
            onCheckedChange = setDone,
        )
        Text("Done")
    }
}

@Composable
private fun DeadlineField(
    deadline: CreateTodoUiState.Deadline,
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
        Text(deadline.formatString())
    }
}

@Composable
private fun ColumnScope.CreateButton(create: () -> Unit) {
    Button(
        onClick = create,
        modifier = Modifier.align(Alignment.End)
    ) {
        Text("Create")
    }
}