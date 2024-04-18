package com.ring.ring.todo.feature.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.util.date.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject


@HiltViewModel
internal class EditTodoViewModel @Inject constructor(
    private val todoRepository: EditTodoRepository,
    private val dateUtil: DateUtil,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val id: Long =
        savedStateHandle.get<Long>(EditTodoNav.ID) ?: throw IllegalArgumentException()
    private var deadline: Instant = dateUtil.currentInstant()
    private val _uiState = MutableStateFlow(initEditTodoUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<EditTodoEvent>(capacity = 5, BufferOverflow.DROP_OLDEST)
    val events = _events.receiveAsFlow()

    private val getErrorHandler = CoroutineExceptionHandler { _, _ ->
        _events.trySend(EditTodoEvent.GetTodoError)
    }
    private val editErrorHandler = CoroutineExceptionHandler { _, _ ->
        _events.trySend(EditTodoEvent.EditError)
    }
    private val deleteErrorHandler = CoroutineExceptionHandler { _, _ ->
        _events.trySend(EditTodoEvent.DeleteError)
    }

    fun setTitle(title: String) {
        if (this.uiState.value.todo.title == title) return
        _uiState.value = uiState.value.replaceTitle(title = title)
    }

    fun setDescription(description: String) {
        if (this.uiState.value.todo.description == description) return
        _uiState.value = uiState.value.replaceDescription(description = description)
    }

    fun setDone(done: Boolean) {
        if (this.uiState.value.todo.done == done) return
        _uiState.value = uiState.value.replaceDone(done = done)
    }

    fun setDeadline(dateMillis: Long) {
        deadline = dateUtil.toInstant(dateMillis)
        _uiState.value = uiState.value.replaceDeadline(deadline = dateUtil.format(deadline))
    }

    fun showDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = true)
    }

    fun dismissDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = false)
    }

    fun getTodo() {
        viewModelScope.launch(getErrorHandler) {
            val response = todoRepository.getTodo(id)
            deadline = response.deadline
            _uiState.value = uiState.value.copy(todo = response.todo)
        }
    }

    fun editTodo() {
        viewModelScope.launch(editErrorHandler) {
            todoRepository.editTodo(
                id = id,
                uiState.value.todo,
                deadline = deadline,
            )
            _events.trySend(EditTodoEvent.EditSuccess)
        }
    }

    fun deleteTodo() {
        viewModelScope.launch(deleteErrorHandler) {
            todoRepository.deleteTodo(id)
            _events.trySend(EditTodoEvent.DeleteSuccess)
        }
    }

    private fun initEditTodoUiState() = EditTodoUiState(
        todo = EditTodoUiState.Todo(
            title = "",
            description = "",
            done = false,
            deadline = dateUtil.format(deadline),
        ),
        isShowDatePicker = false,
    )
}

private fun EditTodoUiState.replaceTitle(title: String): EditTodoUiState {
    return this.copy(
        todo = todo.copy(title = title)
    )
}

private fun EditTodoUiState.replaceDescription(description: String): EditTodoUiState {
    return this.copy(
        todo = todo.copy(description = description)
    )
}

private fun EditTodoUiState.replaceDone(done: Boolean): EditTodoUiState {
    return this.copy(
        todo = todo.copy(done = done)
    )
}

private fun EditTodoUiState.replaceDeadline(deadline: String): EditTodoUiState {
    return this.copy(
        todo = todo.copy(deadline = deadline)
    )
}