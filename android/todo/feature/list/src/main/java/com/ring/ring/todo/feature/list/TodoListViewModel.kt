package com.ring.ring.todo.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.todo.infra.local.LocalTodo
import com.ring.ring.todo.infra.network.dto.ListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _todoList = MutableStateFlow(emptyList<TodoListUiState.Todo>())
    val todoList = _todoList.asStateFlow()

    private val _fetchErrorEvent = Channel<Unit>()
    val fetchErrorEvent = _fetchErrorEvent.receiveAsFlow()
    private val _toggleDoneErrorEvent = Channel<Unit>()
    val toggleDoneErrorEvent = _toggleDoneErrorEvent.receiveAsFlow()

    private val fetchErrorHandler = CoroutineExceptionHandler { _, _ ->
        _fetchErrorEvent.trySend(Unit)
    }
    private val toggleDoneHandler = CoroutineExceptionHandler { _, _ ->
        _toggleDoneErrorEvent.trySend(Unit)
    }

    fun fetchTodoList() {
        viewModelScope.launch(fetchErrorHandler) {
            _todoList.value = todoRepository.list()
        }
    }

    fun toggleDone(todoId: Long) {
        viewModelScope.launch(toggleDoneHandler) {
            val index = findTargetIndex(todoId)
            val newTodo = getTodoWithToggleDone(index)
            todoRepository.editDone(newTodo.id, newTodo.done)
            updateTodoList(index, newTodo)
        }
    }

    private fun findTargetIndex(todoId: Long) = todoList.value.indexOfFirst { it.id == todoId }

    private fun getTodoWithToggleDone(index: Int) =
        todoList.value[index].copy(done = todoList.value[index].done.not())

    private fun updateTodoList(index: Int, newTodo: TodoListUiState.Todo) {
        val newList = todoList.value.toMutableList()
        newList[index] = newTodo
        _todoList.value = newList
    }
}

fun ListResponse.Todo.toLocalTodo(): LocalTodo {
    return LocalTodo(
        id = id,
        title = title,
        description = description,
        done = done,
        deadline = deadline,
    )
}