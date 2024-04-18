package com.ring.ring.todo.feature.create

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
internal class CreateTodoViewModel @Inject constructor(
    private val todoRepository: CreateTodoRepository,
    private val dateUtil: DateUtil,
) : ViewModel() {
    private var deadline: Instant = dateUtil.currentInstant()
    private val _uiState = MutableStateFlow(initUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<CreateTodoEvent>(capacity = 5, BufferOverflow.DROP_OLDEST)
    val event = _event.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, _ ->
        _event.trySend(CreateTodoEvent.CreateTodoError)
    }

    fun saveTodo() {
        viewModelScope.launch(handler) {
            todoRepository.create(
                uiState = uiState.value,
                deadline = deadline
            )
            _event.trySend(CreateTodoEvent.CreateTodoSuccess)
        }
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

    fun setDeadline(dateMillis: Long) {
        deadline = Instant.fromEpochMilliseconds(dateMillis)
        _uiState.value = uiState.value.copy(deadline = dateUtil.format(deadline))
    }

    fun showDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = true)
    }

    fun dismissDatePicker() {
        _uiState.value = uiState.value.copy(isShowDatePicker = false)
    }

    private fun initUiState() = CreateTodoUiState(
        title = "",
        description = "",
        done = false,
        deadline = dateUtil.format(deadline),
        isShowDatePicker = false
    )
}
