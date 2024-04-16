package com.ring.ring.todo.feature.create

import android.content.Context
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ring.ring.todo.feature.create.CreateTodoEvent.CreateTodoError
import com.ring.ring.todo.feature.create.CreateTodoEvent.CreateTodoSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun CreateTodoScreen(
    viewModel: CreateTodoViewModel = hiltViewModel(),
    toTodoListScreen: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    CreateTodoScreen(
        uiState = rememberCreateTodoUiState(viewModel = viewModel),
        updater = remember { toUpdater(viewModel) },
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                CreateTodoSuccess -> toTodoListScreen()
                CreateTodoError -> showCreateTodoFailedSnackbar(snackBarHostState, context, scope)
            }
        }
    }
}

private fun showCreateTodoFailedSnackbar(
    snackBarHostState: SnackbarHostState,
    context: Context,
    scope: CoroutineScope
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = context.getString(R.string.failed_to_create),
            withDismissAction = true,
        )
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
                title = { Text(stringResource(R.string.create_todo)) },
                navigationIcon = { NavigationIcon(toTodoListScreen) }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { padding ->
        Content(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            title = uiState.title,
            description = uiState.description,
            done = uiState.done,
            deadline = uiState.deadline,
            isShowDatePicker = uiState.isShowDatePicker,
            updater = updater,
        )
    }
}

@Composable
private fun NavigationIcon(toTodoListScreen: () -> Unit) {
    IconButton(onClick = toTodoListScreen) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier,
    title: String,
    description: String,
    done: Boolean,
    deadline: String,
    isShowDatePicker: Boolean,
    updater: CreateTodoUiUpdater,
    datePickerState: DatePickerState = rememberDatePickerState(),
    scrollState: ScrollState = rememberScrollState(),
) {
    Box(
        modifier = modifier.verticalScroll(state = scrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TitleTextField(title, updater.setTitle)
            DescriptionTextField(description, updater.setDescription)
            DoneCheckBox(done, updater.setDone)
            DeadlineField(deadline, updater.showDatePicker)
            Spacer(modifier = Modifier.height(8.dp))
            CreateButton(updater.saveTodo)
        }
        CustomDatePicker(
            isShowDatePicker,
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
    setDone: (Boolean) -> Unit,
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
private fun ColumnScope.CreateButton(create: () -> Unit) {
    Button(
        onClick = create,
        modifier = Modifier.align(Alignment.End)
    ) {
        Text(stringResource(R.string.create))
    }
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
                Text(
                    stringResource(R.string.set),
                    modifier = Modifier
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