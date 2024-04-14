package com.ring.ring.todo.feature.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
internal class EditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: Long =
        savedStateHandle.get<Long>(EditTodoNav.ID) ?: throw IllegalArgumentException()
    private var deadline: Instant = Clock.System.now()
    private val _uiState = MutableStateFlow(initEditTodoUiState())
    val uiState = _uiState.asStateFlow()

    private val _editFinishedEvent = Channel<Unit>()
    val editFinishedEvent = _editFinishedEvent.receiveAsFlow()
    private val _deleteFinishedEvent = Channel<Unit>()
    val deleteFinishedEvent = _deleteFinishedEvent.receiveAsFlow()
    private val _editErrorEvent = Channel<Unit>()

    val editErrorEvent = _editErrorEvent.receiveAsFlow()
    private val _deleteErrorEvent = Channel<Unit>()
    val deleteErrorEvent = _deleteErrorEvent.receiveAsFlow()
    private val _getTodoErrorEvent = Channel<Unit>()
    val getTodoErrorEvent = _getTodoErrorEvent.receiveAsFlow()
    private val getErrorHandler = CoroutineExceptionHandler { c, e ->
        _getTodoErrorEvent.trySend(Unit)
    }

    private val editErrorHandler = CoroutineExceptionHandler { _, _ ->
        _editErrorEvent.trySend(Unit)
    }
    private val deleteErrorHandler = CoroutineExceptionHandler { _, _ ->
        _deleteErrorEvent.trySend(Unit)
    }

    fun setTitle(title: String) {
        if (this.uiState.value.title == title) return
        _uiState.value = uiState.value.copy(title = title)
    }

    fun setDescription(description: String) {
        if (this.uiState.value.description == description) return
        _uiState.value = uiState.value.copy(description = description)
    }

    fun setDone(done: Boolean) {
        if (this.uiState.value.done == done) return
        _uiState.value = uiState.value.copy(done = done)
    }

    fun getTodo() {
        viewModelScope.launch(getErrorHandler) {
            val response = todoRepository.getTodo(id)
            deadline = response.deadline
            _uiState.value = uiState.value.copy(
                title = response.title,
                description = response.description,
                done = response.done,
                deadline = DateUtil.format(deadline),
            )
        }
    }

    fun editTodo() {
        viewModelScope.launch(editErrorHandler) {
            todoRepository.editTodo(
                id = id,
                title = uiState.value.title,
                description = uiState.value.description,
                done = uiState.value.done,
                deadline = deadline,
            )
            _editFinishedEvent.trySend(Unit)
        }
    }

    fun deleteTodo() {
        viewModelScope.launch(deleteErrorHandler) {
            todoRepository.deleteTodo(id)
            _deleteFinishedEvent.trySend(Unit)
        }
    }

    fun setDeadline(dateMillis: Long) {
        deadline = Instant.fromEpochMilliseconds(dateMillis)
        _uiState.value = uiState.value.copy(deadline = DateUtil.format(deadline))
    }

    fun showDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = true)
    }

    fun dismissDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = false)
    }

    private fun initEditTodoUiState() = EditTodoUiState(
        title = "",
        description = "",
        done = false,
        deadline = DateUtil.format(deadline),
        isShowDatePicker = false,
    )
}