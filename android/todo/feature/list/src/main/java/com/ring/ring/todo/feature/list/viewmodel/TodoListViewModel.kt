package com.ring.ring.todo.feature.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.todo.feature.list.view.TodoListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoListRepository,
) : ViewModel() {
    val uiState: StateFlow<TodoListUiState> = todoRepository.getTodoListStream()
        .map { TodoListUiState(todoList = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TodoListUiState(todoList = emptyList())
        )

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
            todoRepository.refresh()
        }
    }

    fun toggleDone(todoId: Long) {
        viewModelScope.launch(toggleDoneHandler) {
            findTodo(todoId)?.done?.not()?.let { newDone ->
                todoRepository.editDone(todoId, newDone)
            }
        }
    }

    private fun findTodo(todoId: Long) = uiState.value.todoList.find { it.id == todoId }
}