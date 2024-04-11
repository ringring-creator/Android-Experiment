package com.ring.ring.list

import com.ring.ring.local.LocalTodo
import com.ring.ring.local.LocalUser
import com.ring.ring.test.FakeTodoLocalDataSource
import com.ring.ring.test.FakeTodoNetworkDataSource
import com.ring.ring.test.FakeUserLocalDataSource
import com.ring.ring.test.MainDispatcherRule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TodoListViewModelTest {
    private lateinit var subject: TodoListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var networkDataSource = FakeTodoNetworkDataSource()
    private var localDataSource = FakeTodoLocalDataSource()
    private var userLocalDataSource = FakeUserLocalDataSource()
    private var localUser = LocalUser(10L, "fakeEmail", "fakeToken")

    @Before
    fun setUp() {
        runBlocking { userLocalDataSource.save(localUser) }
        networkDataSource.parameter =
            FakeTodoNetworkDataSource.Parameter(localUser.userId, localUser.token)

        setupSubject()
    }

    @Test
    fun `fetchTodoList set todoList fetched from network`() = runTest {
        //given,when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.todoList.value
        assertThat(todoList.count(), equalTo(2))

        val firstElement = todoList.first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo("fakeTitle"))
        assertThat(firstElement.done, equalTo(false))
        assertThat(firstElement.deadline, equalTo("2024-01-01T00:00:00Z"))
    }

    @Test
    fun `fetchTodoList save local todoList fetched from network`() = runTest {
        //given,when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val actual = localDataSource.list()

        assertThat(actual.count(), equalTo(2))
        val firstElement = actual.first()
        val expected = networkDataSource.listResponse.first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.description, equalTo(expected.description))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(expected.deadline))
    }

    @Test
    fun `fetchTodoList set todoList fetched from local when network failed`() = runTest {
        //given
        val expected = LocalTodo(
            id = 1L,
            title = "fakeTitle",
            description = "fakeDescription",
            done = true,
            deadline = Instant.parse("2024-04-01T00:00:00Z"),
        )
        localDataSource.upsert(listOf(expected, expected.copy(id = 2L)))
        networkDataSource = FakeTodoNetworkDataSource(true)
        setupSubject()

        //when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.todoList.value
        assertThat(todoList.count(), equalTo(2))

        val firstElement = todoList.first()
        assertThat(firstElement.id, equalTo(1L))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo("2024-04-01"))
    }

    @Test
    fun toggleDone() {
    }

    private fun setupSubject() {
        subject = TodoListViewModel(
            todoRepository = TodoRepository(
                networkDataSource = networkDataSource,
                localDataSource = localDataSource,
                userLocalDataSource = userLocalDataSource,
            ),
        )
    }
}
