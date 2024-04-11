package com.ring.ring.list

data class TodoListUiUpdater(
    val toggleDone: (todoId: Long) -> Unit
)

internal fun toUpdater(viewModel: TodoListViewModel) = TodoListUiUpdater(
    toggleDone = viewModel::toggleDone,
)