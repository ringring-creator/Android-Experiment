package com.ring.ring.todo.feature.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.todo.feature.list.view.TodoListUiState
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
    private val todoRepository: TodoListRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TodoListUiState(todoList = emptyList()))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<TodoListEvent>()
    val event = _event.receiveAsFlow()

    private val fetchErrorHandler = CoroutineExceptionHandler { _, _ ->
        _event.trySend(TodoListEvent.FetchErrorEvent)
    }
    private val toggleDoneHandler = CoroutineExceptionHandler { _, _ ->
        _event.trySend(TodoListEvent.ToggleDoneErrorEvent)
    }

    fun fetchTodoList() {
        viewModelScope.launch(fetchErrorHandler) {
            val todoList = todoRepository.list()
            _uiState.value = uiState.value.copy(todoList = todoList)
            todoRepository.refresh()
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

    private fun getTodoWithToggleDone(index: Int): TodoListUiState.Todo =
        uiState.value.todoList[index].copy(done = uiState.value.todoList[index].done.not())

    private fun updateTodoList(index: Int, newTodo: TodoListUiState.Todo) {
        val newList = uiState.value.todoList.toMutableList()
        newList[index] = newTodo
        _uiState.value = uiState.value.copy(todoList = newList)
    }
}