package com.ring.ring.todo.feature.list.view

import com.ring.ring.todo.feature.list.viewmodel.TodoListViewModel

data class TodoListUiUpdater(
    val toggleDone: (todoId: Long) -> Unit
)

internal fun toUpdater(viewModel: TodoListViewModel) = TodoListUiUpdater(
    toggleDone = viewModel::toggleDone,
)