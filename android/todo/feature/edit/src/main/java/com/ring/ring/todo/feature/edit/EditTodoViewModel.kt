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
class EditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: Long =
        savedStateHandle.get<Long>(EditTodoNav.ID) ?: throw IllegalArgumentException()
    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _done: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val done = _done.asStateFlow()
    private val _deadline: MutableStateFlow<EditTodoUiState.Deadline> = MutableStateFlow(
        EditTodoUiState.Deadline(Clock.System.now().toEpochMilliseconds())
    )
    val deadline = _deadline.asStateFlow()
    private val _isShowDatePicker: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isShowDatePicker = _isShowDatePicker.asStateFlow()

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
        if (this.title.value == title) return
        _title.value = title
    }

    fun setDescription(description: String) {
        if (this.description.value == description) return
        _description.value = description
    }

    fun setDone(done: Boolean) {
        if (this.done.value == done) return
        _done.value = done
    }

    fun getTodo() {
        viewModelScope.launch(getErrorHandler) {
            val response = todoRepository.getTodo(id)
            _title.value = response.title
            _description.value = response.description
            _done.value = response.done
            _deadline.value = EditTodoUiState.Deadline(response.deadline.toEpochMilliseconds())
        }
    }

    fun editTodo() {
        viewModelScope.launch(editErrorHandler) {
            todoRepository.editTodo(
                id = id,
                title = title.value,
                description = description.value,
                done = done.value,
                deadline = Instant.fromEpochMilliseconds(deadline.value.dateMillis),
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
        _deadline.value = EditTodoUiState.Deadline(dateMillis)
    }

    fun showDatePicker() {
        _isShowDatePicker.value = true
    }

    fun dismissDatePicker() {
        _isShowDatePicker.value = false
    }
}