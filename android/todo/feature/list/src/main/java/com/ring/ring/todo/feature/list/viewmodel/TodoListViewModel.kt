package com.ring.ring.todo.feature.list.viewmodel

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.ring.ring.todo.feature.list.view.TodoListUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class TodoListViewModel @AssistedInject constructor(
    @Assisted initialState: TodoListUiState,
    private val todoRepository: TodoListRepository,
) : MavericksViewModel<TodoListUiState>(initialState) {
    init {
        viewModelScope.launch {
            todoRepository.getTodoListStream().collect {
                setState { TodoListUiState(todoList = it) }
            }
        }
    }

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
        withState { state ->
            viewModelScope.launch(toggleDoneHandler) {
                findTodo(state, todoId)?.done?.not()?.let { newDone ->
                    todoRepository.editDone(todoId, newDone)
                }
            }
        }
    }

    private fun findTodo(
        state: TodoListUiState, todoId: Long
    ) = state.todoList.find { it.id == todoId }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<TodoListViewModel, TodoListUiState> {
        override fun create(state: TodoListUiState): TodoListViewModel
    }

    companion object :
        MavericksViewModelFactory<TodoListViewModel, TodoListUiState> by hiltMavericksViewModelFactory() {
        override fun initialState(viewModelContext: ViewModelContext): TodoListUiState {
            return TodoListUiState(todoList = emptyList())
        }
    }
}