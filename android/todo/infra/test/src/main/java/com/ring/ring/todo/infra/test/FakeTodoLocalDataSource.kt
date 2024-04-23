package com.ring.ring.todo.infra.test

import com.ring.ring.todo.infra.domain.Todo
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTodoLocalDataSource(
    values: List<Todo> = emptyList(),
) : TodoLocalDataSource {
    private val _todoList = MutableStateFlow(values)
    override fun getTodoListStream(): Flow<List<Todo>> {
        return _todoList.asStateFlow()
    }

    override suspend fun upsert(todoList: List<Todo>) {
        val newList = _todoList.value.toMutableList()
        newList.addAll(todoList)
        _todoList.value = newList
    }

    override suspend fun deleteAll() {
        val newList = _todoList.value.toMutableList()
        newList.clear()
        _todoList.value = newList
    }

    override suspend fun updateDone(id: Long, done: Boolean) {
        val index = _todoList.value.indexOfFirst { it.id == id }
        val newList = _todoList.value.toMutableList()
        newList[index] = _todoList.value[index].copy(done = done)
        _todoList.value = newList
    }
}