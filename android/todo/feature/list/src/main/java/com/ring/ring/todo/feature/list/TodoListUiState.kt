package com.ring.ring.todo.feature.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ring.ring.todo.infra.local.LocalTodo

internal data class TodoListUiState(
    val todoList: List<Todo>
) {
    internal data class Todo(
        val id: Long,
        val title: String,
        val done: Boolean,
        val deadline: String,
    )
}

internal fun LocalTodo.toTodo(): TodoListUiState.Todo? {
    return TodoListUiState.Todo(
        id = id ?: return null,
        title = title,
        done = done,
        deadline = DateUtil.format(deadline),
    )
}

@Composable
internal fun rememberTodoListUiState(
    viewModel: TodoListViewModel,
): TodoListUiState {
    val todoList by viewModel.todoList.collectAsState()
    return TodoListUiState(
        todoList = todoList,
    )
}