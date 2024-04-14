package com.ring.ring.todo.feature.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ring.ring.todo.infra.domain.Todo

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

internal fun Todo.toTodo(): TodoListUiState.Todo? {
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
    val todoList by viewModel.todoList.collectAsStateWithLifecycle()
    return TodoListUiState(
        todoList = todoList,
    )
}