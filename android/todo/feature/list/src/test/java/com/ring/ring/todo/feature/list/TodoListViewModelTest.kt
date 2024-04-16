package com.ring.ring.todo.feature.list

import com.ring.ring.infra.test.MainDispatcherRule
import com.ring.ring.todo.infra.domain.TodoLocalDataSource
import com.ring.ring.todo.infra.domain.TodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeErrorTodoNetworkDataSource
import com.ring.ring.todo.infra.test.FakeTodoLocalDataSource
import com.ring.ring.todo.infra.test.FakeTodoNetworkDataSource
import com.ring.ring.user.infra.model.User
import com.ring.ring.user.infra.test.FakeUserLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private lateinit var subject: TodoListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private var localUser = User.generate(10L, "email@example.com", "Abcdefg1")
    private var networkDataSource: TodoNetworkDataSource = FakeTodoNetworkDataSource(
        parameter = FakeTodoNetworkDataSource.Parameter(localUser.id.value, localUser.token)
    )
    private var localDataSource: TodoLocalDataSource = FakeTodoLocalDataSource()
    private var userLocalDataSource = FakeUserLocalDataSource()

    @Before
    fun setUp() {
        runBlocking { userLocalDataSource.save(localUser) }

        setupSubject()
    }

    @Test
    fun `fetchTodoList set todoList fetched from network`() = runTest {
        //given,when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.uiState.value.todoList
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
        val expected = networkDataSource.list(localUser.token).first()
        assertThat(firstElement.id, equalTo(1))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.description, equalTo(expected.description))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(expected.deadline))
    }

    @Test
    fun `fetchTodoList set todoList fetched from local when network failed`() = runTest {
        //given
        networkDataSource = FakeErrorTodoNetworkDataSource()
        setupSubject()

        //when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        val todoList = subject.uiState.value.todoList
        assertThat(todoList.count(), equalTo(2))

        val firstElement = todoList.first()
        val expected = localDataSource.list().first()
        assertThat(firstElement.id, equalTo(expected.id))
        assertThat(firstElement.title, equalTo(expected.title))
        assertThat(firstElement.done, equalTo(expected.done))
        assertThat(firstElement.deadline, equalTo(DateUtil.format(expected.deadline)))
    }

    @Test
    fun `fetchTodoList send fetchErrorEvent when local failed`() = runTest {
        //given
        networkDataSource = FakeErrorTodoNetworkDataSource()
        localDataSource = FakeErrorTodoLocalDataSource()
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == TodoListEvent.FetchErrorEvent) wasCalled = true
            }
        }

        //when
        subject.fetchTodoList()
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
    }

    @Test
    fun `toggleDone update done of todo in network`() = runTest {
        //given
        subject.fetchTodoList()

        //when
        subject.toggleDone(1L)
        advanceUntilIdle()

        //then
        val actual = networkDataSource
            .list(localUser.token)
            .find { it.id == 1L }!!
        assertThat(actual.done, `is`(true))
    }

    @Test
    fun `toggleDone refresh todoList`() = runTest {
        //given
        subject.fetchTodoList()

        //when
        subject.toggleDone(1L)
        advanceUntilIdle()

        //then
        val actual = subject.uiState.value.todoList.find { it.id == 1L }
        assertThat(actual!!.done, `is`(true))
    }

    @Test
    fun `toggleDone send toggleDoneErrorEvent when editDone failed`() = runTest {
        //given
        networkDataSource = FakeErrorTodoNetworkDataSource()
        setupSubject()
        var wasCalled = false
        TestScope(UnconfinedTestDispatcher()).launch {
            subject.event.collect {
                if (it == TodoListEvent.ToggleDoneErrorEvent) wasCalled = true
            }
        }

        //when
        subject.toggleDone(1L)
        advanceUntilIdle()

        //then
        assertThat(wasCalled, `is`(true))
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
