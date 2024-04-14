package com.ring.ring.todo.feature.edit

data class EditTodoUiUpdater(
    val editTodo: () -> Unit,
    val deleteTodo: () -> Unit,
    val setTitle: (title: String) -> Unit,
    val setDescription: (description: String) -> Unit,
    val setDone: (done: Boolean) -> Unit,
    val setDeadline: (dateMillis: Long) -> Unit,
    val showDatePicker: () -> Unit,
    val dismissDatePicker: () -> Unit,
)

internal fun toEditTodoUiUpdater(viewModel: EditTodoViewModel): EditTodoUiUpdater {
    return EditTodoUiUpdater(
        editTodo = viewModel::editTodo,
        deleteTodo = viewModel::deleteTodo,
        setTitle = viewModel::setTitle,
        setDescription = viewModel::setDescription,
        setDone = viewModel::setDone,
        setDeadline = viewModel::setDeadline,
        showDatePicker = viewModel::showDatePicker,
        dismissDatePicker = viewModel::dismissDatePicker,
    )
}