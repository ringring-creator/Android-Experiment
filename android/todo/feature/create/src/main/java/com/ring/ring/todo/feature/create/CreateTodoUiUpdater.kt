package com.ring.ring.todo.feature.create

data class CreateTodoUiUpdater(
    val saveTodo: () -> Unit,
    val setTitle: (title: String) -> Unit,
    val setDescription: (description: String) -> Unit,
    val setDone: (done: Boolean) -> Unit,
    val setDeadline: (dateMillis: Long) -> Unit,
    val showDatePicker: () -> Unit,
    val dismissDatePicker: () -> Unit,
)

internal fun toUpdater(viewModel: CreateTodoViewModel) = CreateTodoUiUpdater(
    saveTodo = viewModel::saveTodo,
    setTitle = viewModel::setTitle,
    setDescription = viewModel::setDescription,
    setDone = viewModel::setDone,
    setDeadline = viewModel::setDeadline,
    showDatePicker = viewModel::showDatePicker,
    dismissDatePicker = viewModel::dismissDatePicker,
)