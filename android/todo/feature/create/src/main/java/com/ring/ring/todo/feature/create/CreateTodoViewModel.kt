package com.ring.ring.todo.feature.create

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
internal class CreateTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _done = MutableStateFlow(false)
    val done = _done.asStateFlow()
    private val _deadline = MutableStateFlow(
        CreateTodoUiState.Deadline(Clock.System.now().toEpochMilliseconds())
    )
    val deadline = _deadline.asStateFlow()
    private val _isShowDatePicker = MutableStateFlow(false)
    val isShowDatePicker = _isShowDatePicker.asStateFlow()

    private val _saveSuccessEvent = Channel<Unit>()
    val saveSuccessEvent = _saveSuccessEvent.receiveAsFlow()
    private val _saveTodoErrorEvent = Channel<Unit>()
    val saveTodoErrorEvent = _saveTodoErrorEvent.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _saveTodoErrorEvent.trySend(Unit)
    }

    fun saveTodo() {
        viewModelScope.launch(handler) {
            todoRepository.create(
                title = title.value,
                description = description.value,
                done = done.value,
                deadline = Instant.fromEpochMilliseconds(deadline.value.dateMillis)
            )
            _saveSuccessEvent.trySend(Unit)
        }
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

    fun setDeadline(dateMillis: Long) {
        _deadline.value = CreateTodoUiState.Deadline(dateMillis)
    }

    fun showDatePicker() {
        _isShowDatePicker.value = true
    }

    fun dismissDatePicker() {
        _isShowDatePicker.value = false
    }
}
