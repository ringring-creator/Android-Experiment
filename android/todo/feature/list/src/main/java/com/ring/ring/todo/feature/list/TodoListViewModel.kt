package com.ring.ring.todo.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _uiState = MutableStateFlow(TodoListUiState(todoList = emptyList()))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<TodoListEvent>()
    val event = _event.receiveAsFlow()
//    private val _fetchErrorEvent = Channel<Unit>()
//    val fetchErrorEvent = _fetchErrorEvent.receiveAsFlow()
//    private val _toggleDoneErrorEvent = Channel<Unit>()
//    val toggleDoneErrorEvent = _toggleDoneErrorEvent.receiveAsFlow()

    private val fetchErrorHandler = CoroutineExceptionHandler { _, _ ->
        _event.trySend(TodoListEvent.FetchErrorEvent)
    }
    private val toggleDoneHandler = CoroutineExceptionHandler { _, _ ->
        _event.trySend(TodoListEvent.ToggleDoneErrorEvent)
    }

    fun fetchTodoList() {
        viewModelScope.launch(fetchErrorHandler) {
            _uiState.value = uiState.value.copy(todoList = todoRepository.list())
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

    private fun findTargetIndex(todoId: Long) =
        uiState.value.todoList.indexOfFirst { it.id == todoId }

    private fun getTodoWithToggleDone(index: Int) =
        uiState.value.todoList[index].copy(done = uiState.value.todoList[index].done.not())

    private fun updateTodoList(index: Int, newTodo: TodoListUiState.Todo) {
        val newList = uiState.value.todoList.toMutableList()
        newList[index] = newTodo
        _uiState.value = uiState.value.copy(todoList = newList)
    }
}